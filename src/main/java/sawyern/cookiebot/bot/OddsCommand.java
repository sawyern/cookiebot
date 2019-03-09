package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.util.BotUtil;

@Component
public class OddsCommand extends GenericBotCommand {
    @Override
    public String getCommand() {
        return "odds";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        StringBuilder builder = new StringBuilder("```");
        builder.append("0: " + LootBoxCommand.getZERO() + "%\n");
        builder.append("1: " + LootBoxCommand.getONE() + "%\n");
        builder.append("2: " + LootBoxCommand.getTWO() + "%\n");
        builder.append("3: " + LootBoxCommand.getTHREE() + "%\n");
        builder.append("4: " + LootBoxCommand.getFOUR() + "%\n");
        builder.append("5: " + LootBoxCommand.getFIVE() + "%\n");
        builder.append("6: " + LootBoxCommand.getSIX() + "%\n");
        builder.append("7: " + LootBoxCommand.getSEVEN() + "%\n");
        builder.append("```");
        BotUtil.sendMessage(event, builder.toString());
    }
}
