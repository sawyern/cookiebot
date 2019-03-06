package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.CommandConstants;

@Component
public class PingCommand extends GenericBotCommand {
    @Override
    public void execute(MessageCreateEvent event) {
        sendMessage(event, CommandConstants.PONG);
    }

    @Override
    public String getCommand() {
        return "ping";
    }
}
