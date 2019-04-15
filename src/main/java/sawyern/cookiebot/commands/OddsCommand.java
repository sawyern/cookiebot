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
        builder.append("0: ").append(LootBoxCommand.getZERO()).append("%\n");
        builder.append("1: ").append(LootBoxCommand.getONE()).append("%\n");
        builder.append("2: ").append(LootBoxCommand.getTWO()).append("%\n");
        builder.append("3: ").append(LootBoxCommand.getTHREE()).append("%\n");
        builder.append("4: ").append(LootBoxCommand.getFOUR()).append("%\n");
        builder.append("5: ").append(LootBoxCommand.getFIVE()).append("%\n");
        builder.append("6: ").append(LootBoxCommand.getSIX()).append("%\n");
        builder.append("7: ").append(LootBoxCommand.getSEVEN()).append("%\n");
        builder.append("```");
        botUtil.sendMessage(event, builder.toString());
    }
}
