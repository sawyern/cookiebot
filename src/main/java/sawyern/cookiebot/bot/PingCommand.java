package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.util.BotUtil;

@Component
public class PingCommand extends GenericBotCommand {
    @Override
    public void execute(MessageCreateEvent event) throws CookieException  {
        BotUtil.sendMessage(event, CommandConstants.PONG);
    }

    @Override
    public String getCommand() {
        return "ping";
    }
}
