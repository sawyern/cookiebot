package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.LootboxTokenService;
import sawyern.cookiebot.util.BotUtil;

@Component
@RequiredArgsConstructor
public class TokensCommand extends GenericBotCommand {

    private final LootboxTokenService lootboxTokenService;

    @Override
    public String getCommand() {
        return "tokens";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        String id = BotUtil.getMember(event).getId().asString();
        int numLootboxes = lootboxTokenService.getAllByAccount(id).size();
        BotUtil.sendMessage(event, BotUtil.getMember(event).getUsername() + " has " + numLootboxes + " tokens.");
    }
}
