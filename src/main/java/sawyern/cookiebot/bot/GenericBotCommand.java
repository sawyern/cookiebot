package sawyern.cookiebot.bot;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sawyern.cookiebot.bot.exception.InvalidMessageCookieException;
import sawyern.cookiebot.bot.exception.MessageParseCommandCookieException;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.bot.exception.CookieException;
import sawyern.cookiebot.bot.exception.DiscordException;
import sawyern.cookiebot.util.BotUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class GenericBotCommand implements BotCommand {
    private List<String> args;

    private static Logger LOGGER = LoggerFactory.getLogger(GenericBotCommand.class);

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
            if (parseCommand(event.getMessage()
                    .getContent()
                    .orElseThrow(DiscordException::new))
            ) {
                LOGGER.info(MessageFormat.format("Received command: {0}", getCommand()));

                // execute command
                execute(event);
            }
        } catch (CookieException e) {
            // known exception caught and handled. printed here
            BotUtil.sendMessage(event, e.getMessage());
        } catch (DiscordException e) {
            // text message was not sent. could be image etc. ignore this case
            return;
        } catch (Exception e) {
            // log unknown exceptions
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * name of command
     * @return the name of the command
     */
    public abstract String getCommand();

    /**
     * returns true if input string starts with "!" and matches getCommand()
     * @param message input message
     * @return true if input string starts with "!" and matches getCommand()
     * @see #getCommand()
     */
    public boolean parseCommand(String message) throws CookieException {
        this.args = parseArgs(message);
        String command = args.stream().findFirst().orElseThrow(MessageParseCommandCookieException::new);
        if (command.startsWith(CommandConstants.COMMAND_START)) {
            return command.substring(1).equalsIgnoreCase(getCommand());
        } else return false;
    }

    /**
     * splits the message by " " into a list of string arguments.
     * if argument is surrounded by quotes, it will consider the block as one argument
     * @param message input message
     * @return list of arguments
     */
    private List<String> parseArgs(String message) throws CookieException {
        if (message == null || message.isEmpty())
            throw new InvalidMessageCookieException();
        List<String> msgArgs = new ArrayList<>();
        Matcher m = Pattern.compile(CommandConstants.QUOTE_REGEX).matcher(message);
        while (m.find())
            msgArgs.add(m.group(1).replace(CommandConstants.QUOTE, CommandConstants.EMPTY_STRING));
        return msgArgs;
    }

    public List<String> getArgs() {
        return args;
    }
}
