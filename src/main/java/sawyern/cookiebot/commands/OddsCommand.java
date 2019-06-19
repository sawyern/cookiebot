package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;

import java.util.List;

@Component
public class OddsCommand extends MessageCreateEventBotCommand {
    @Override
    public String getCommand() {
        return "odds";
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        StringBuilder builder = new StringBuilder("```");
        builder.append("50% chance of 0, can titanforge indefinitely. Cookieboxes are 75% 0.```");
        getBotUtil().sendMessage(event, builder.toString());
    }
}
