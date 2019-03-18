package sawyern.cookiebot.bot;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sawyern.cookiebot.exception.InvalidMessageCookieException;
import sawyern.cookiebot.exception.MessageParseCommandCookieException;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.exception.DiscordException;
import sawyern.cookiebot.util.BotUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
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

    protected void executeCommand(MessageCreateEvent event) {
        try {
            String content = event.getMessage().getContent().orElseThrow(DiscordException::new);
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
     * default is 0, override to change. Must not return null
     * @return a list of acceptable argument lengths
     * @see #checkArgs()
     */
    public List<Integer> allowedNumArgs() {
        //default number of arguments is 0
        return Collections.singletonList(0);
    }

    protected final void checkArgs() throws CookieException {
        if (!allowedNumArgs().contains(getArgs().size()))
            throw new CookieException(MessageFormat.format("Invalid number of arguments. Expected {0}", allowedNumArgs()));
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
        parseArgs(message);
        String command = args.stream().findFirst().orElseThrow(MessageParseCommandCookieException::new);
        return command.substring(1).equalsIgnoreCase(getCommand());
    }

    /**
     * splits the message by " " into a list of string arguments.
     * if argument is surrounded by quotes, it will consider the block as one argument
     * sets local msgArgs variable
     * @param message input message
     */
    private void parseArgs(String message) throws CookieException {
        if (message == null || message.isEmpty())
            throw new InvalidMessageCookieException();
        List<String> msgArgs = new ArrayList<>();
        Matcher m = Pattern.compile(CommandConstants.QUOTE_REGEX).matcher(message);
        while (m.find())
            msgArgs.add(m.group(1).replace(CommandConstants.QUOTE, CommandConstants.EMPTY_STRING));
        this.args = msgArgs;
        checkArgs();
    }

    public List<String> getArgs() throws CookieException {
        if (this.args == null)
            throw new CookieException("Args not yet parsed.");
        return args;
    }

}
