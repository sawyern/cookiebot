package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.util.BotUtil;

import java.util.Arrays;
import java.util.List;

@Component
public class PingCommand extends GenericBotCommand {
    @Override
    public void execute(MessageCreateEvent event) throws CookieException  {
        BotUtil.sendMessage(event, CommandConstants.PONG);
    }

    @Override
    public List<Integer> allowedNumArgs() {
        // ignore first argument for testing
        return Arrays.asList(0, 1);
    }

    @Override
    public String getCommand() {
        return "ping";
    }
}
