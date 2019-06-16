package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.LootboxTokenService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TokensCommand extends MessageCreateEventBotCommand {

    private final LootboxTokenService lootboxTokenService;

    @Override
    public String getCommand() {
        return "tokens";
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        String id = getBotUtil().getMember(event).getId().asString();
        int numLootboxes = lootboxTokenService.getAllByAccount(id).size();
        getBotUtil().sendMessage(event, getBotUtil().getMember(event).getUsername() + " has " + numLootboxes + " tokens.");
    }
}
