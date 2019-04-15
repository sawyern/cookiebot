package sawyern.cookiebot.commands;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sawyern.cookiebot.exception.*;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.util.BotUtil;
import sawyern.cookiebot.util.MessageUtil;
import sawyern.cookiebot.util.ThrowingConsumerWrapper;

import java.util.*;

@Component
@Slf4j
public abstract class MessageCreateEventBotCommand implements BotCommand {

    protected BotUtil botUtil;

    /**
     * subscribes this command to be executed when the input message starts with "!" and matches getCommand()
     * @param client discord client
     */
    @Override
    public void subscribeCommand(@NonNull DiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(
                ThrowingConsumerWrapper.throwingConsumerWrapper(this::executeCommand, CookieException.class)
        );
    }

    /**
     * @return the name of the command
     *
     * ex. message content -> !myCommand arg1 arg2
     *     getCommand() returns "myCommand"
     */
    public abstract String getCommand();

    /**
     * empty is default and means infinite arguments allowed, override to change.
     * @return a list of acceptable argument lengths
     */
    public Set<Integer> getAllowedNumArgs() {
        //default number of arguments is infinite
        return new LinkedHashSet<>();
    }

    /**
     * parse command and execute
     * @param event discord message event object
     * @see #getCommand()
     * @see #execute
     */
    final void executeCommand(@NonNull MessageCreateEvent event) throws CookieException {
        try {
            // get text content from message, ignore non-text messages
            String content = event.getMessage().getContent().orElseThrow(DiscordException::new);

            // only process messages that start with "!", optimization
            if (!content.startsWith(CommandConstants.COMMAND_START))
                throw new DiscordException();

            String command = parseCommand(content);
            List<String> args = parseArgs(content);

            if (command.equals(getCommand())) {
                log.info("Received command: {}", getCommand());

                checkValidNumArgs(args);

                execute(event, args);
            }
        } catch (CookieException e) {
            // known exception caught and handled. printed here
            log.error(e.getMessage());
            botUtil.sendMessage(event, e.getMessage());
        } catch (DiscordException e) {
            // ignore this message
        } catch (Exception e) {
            // log unknown exceptions
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Checks to make sure that a valid set of
     * @throws CookieException if invalid number of arguments
     */
    final void checkValidNumArgs(@NonNull List<String> args) throws CookieException {
        // empty means any number of arguments is okay
        if (getAllowedNumArgs().isEmpty())
            return;

        if (!getAllowedNumArgs().contains(args.size()))
            throw new InvalidCommandArgumentLengthCookieException(InvalidCommandArgumentLengthCookieException.getMessageIfSet(getAllowedNumArgs()));
    }

    /**
     * Splits string and return all but the first element
     * @param message to be split
     * @return list of arguments derived from input string
     */
    final List<String> parseArgs(@NonNull String message) {
        Assert.isTrue(!message.isEmpty(), AssertMessage.EMPTY_MESSAGE);
        List<String> splitMessage = MessageUtil.splitMessage(message);

        // ignore first elt as it is the command, only return list of args
        splitMessage.remove(0);

        return splitMessage;
    }

    /**
     * Splits string and return the first element without the "!"
     * @param message to be spkit
     * @return name of command received
     */
    final String parseCommand(@NonNull String message) {
        Assert.isTrue(!message.isEmpty(), AssertMessage.EMPTY_MESSAGE);
        String command = MessageUtil.splitMessage(message)
                .stream()
                .findFirst()
                .orElse(CommandConstants.COMMAND_START + CommandConstants.CommandName.UNKNOWN);

        if (!command.startsWith(CommandConstants.COMMAND_START))
            return CommandConstants.CommandName.UNKNOWN;

        return command.split(CommandConstants.COMMAND_START)[1];
    }

    @Autowired
    public void setBotUtil(BotUtil botUtil) {
        this.botUtil = botUtil;
    }
}
