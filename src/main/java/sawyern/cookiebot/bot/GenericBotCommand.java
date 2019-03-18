package sawyern.cookiebot.bot;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sawyern.cookiebot.exception.*;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.util.BotUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class GenericBotCommand implements BotCommand {
    private List<String> args;

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericBotCommand.class);

    /**
     * subscribes this command to be executed when the input message starts with "!" and matches getCommand()
     * @param client discord client
     */
    @Override
    public void subscribeCommand(DiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(this::executeCommand);
    }

    /**
     * parse command and execute if command matches getCommand()
     * @param event
     * @see #getCommand()
     * @see #execute
     */
    protected void executeCommand(MessageCreateEvent event) {
        try {
            String content = event.getMessage().getContent().orElseThrow(DiscordException::new);
            parseArgs(content);
            if (parseCommand(content)) {
                LOGGER.info("Received command: {}", getCommand());

                // execute command
                execute(event);
            }
        } catch (CookieException e) {
            // known exception caught and handled. printed here
            BotUtil.sendMessage(event, e.getMessage());
        } catch (DiscordException e) {
            // text message was not sent. could be image etc. ignore this case
        } catch (Exception e) {
            // log unknown exceptions
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * @return the name of the command
     *
     * ex. Input: !myCommand arg1 arg2
     *     getCommand() returns "myCommand"
     */
    public abstract String getCommand();

    /**
     * empty is default and means infinite arguments allowed, override to change. Cannot contain negative integers.
     * @return a list of acceptable argument lengths
     * @see #checkArgs()
     */
    public List<Integer> allowedNumArgs() {
        //default number of arguments is infinite
        return new ArrayList<>();
    }

    /**
     * throws CookieException if getArgs().size() is not within the allowedNumArgs list
     * @throws CookieException if invalid number of arguments
     */
    protected final void checkArgs() throws CookieException {
        // null means any number of arguments is okay
        if (allowedNumArgs().isEmpty())
            return;

        // !command does not count as an argument but is included in the argument list, thus 1 is subtracted from size
        if (!allowedNumArgs().contains(getArgs().size() - 1))
            throw new InvalidCommandArgumentLengthCookieException(allowedNumArgs());
    }

    /**
     * returns true if input string starts with "!" and matches getCommand()
     * @param message input message
     * @return true if input string starts with "!" and matches getCommand()
     * @see #getCommand()
     */
    public boolean parseCommand(String message) throws CookieException {
        if (message == null || !message.startsWith(CommandConstants.COMMAND_START))
            return false;
        String command = getArgs().stream().findFirst().orElseThrow(MessageParseCommandCookieException::new);
        return command.substring(1).equalsIgnoreCase(getCommand());
    }

    /**
     * splits the message by " " into a list of string arguments.
     * if argument is surrounded by quotes, it will consider the block as one argument
     * sets local msgArgs variable
     * argument 0 is the command itself
     * @param message input message
     */
    protected void parseArgs(String message) throws CookieException {
        if (message == null || message.isEmpty())
            throw new InvalidMessageCookieException();
        List<String> msgArgs = new ArrayList<>();
        Matcher m = Pattern.compile(CommandConstants.QUOTE_REGEX).matcher(message);
        while (m.find())
            msgArgs.add(m.group(1).replace(CommandConstants.QUOTE, CommandConstants.EMPTY_STRING));
        this.args = msgArgs;
        checkArgs();
    }

    /**
     * @return list of arguments
     * @throws CookieException if parseArgs has not been called yet
     */
    public List<String> getArgs() throws CookieException {
        if (this.args == null)
            throw new ArgsNotParsedCookieException();
        return args;
    }

}
