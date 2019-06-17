package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.CookieType;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.services.LootboxTokenService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LootBoxCommand extends MessageCreateEventBotCommand {

    private final CookieService cookieService;
    private final LootboxTokenService lootboxTokenService;

    private static final Integer COST = 1;

    @Override
    public String getCommand() {
        return "lootbox";
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        int numCookiesWon = 0;
        boolean useToken = false;
        boolean titanForging = true;

        while (titanForging) {
            titanForging = roll() < 25;
            numCookiesWon++;
        }

        String id = getBotUtil().getMember(event).getId().asString();

        if (!lootboxTokenService.getAllByAccount(id).isEmpty())
            useToken = true;
        else if (cookieService.getAllCookiesForAccount(id) < COST)
            throw new CookieException("Lootbox costs 1 cookie. Not enough cookies to buy.");

        if (useToken) {
            lootboxTokenService.deleteLootBoxToken(id);
            getBotUtil().sendMessage(event, "Spending token. Remaining tokens: " + lootboxTokenService.getAllByAccount(id).size());
        }
        else {
            cookieService.removeCookieOfType(id, CookieType.NORMAL);
            getBotUtil().sendMessage(event, "Spending cookie. Remaining cookies: " + cookieService.getAllCookiesForAccount(id));
        }

        cookieService.generateCookie(id, CookieType.NORMAL, numCookiesWon);
        int newTotal = cookieService.getAllCookiesForAccount(id);

        getBotUtil().sendMessage(event, "```You open the lootbox!\nContains..." + numCookiesWon + " cookies!\n" +
                getBotUtil().getMember(event).getUsername() + " cookies: " + newTotal + "\n" +
                getBotUtil().getMember(event).getUsername() + " tokens: " + lootboxTokenService.getAllByAccount(id).size() + "```");
    }

    private double roll() {
        int max = 100;
        int min = 1;
        return Math.random() * ((max - min) + 1) + min;
    }
}


