package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.CookieType;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.services.LootboxTokenService;
import sawyern.cookiebot.util.BotUtil;

@Component
public class LootBoxCommand extends GenericBotCommand {

    private CookieService cookieService;
    private LootboxTokenService lootboxTokenService;

    private static Integer COST = 1;

    private static double ZERO = 30.3125d;
    private static double ONE = 45d;
    private static double TWO = 15d;
    private static double THREE = 5d;
    private static double FOUR = 2.5d;
    private static double FIVE = 1.25d;
    private static double SIX = .625d;
    private static double SEVEN = 0.3125d;


    @Override
    public String getCommand() {
        return "lootbox";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        double roll = roll();
        int numCookiesWon;
        boolean useToken = false;

        if (roll <= SEVEN)
            numCookiesWon = 7;
        else if (roll <= SIX)
            numCookiesWon = 6;
        else if (roll <= FIVE)
            numCookiesWon = 5;
        else if (roll <= FOUR)
            numCookiesWon = 4;
        else if (roll <= THREE)
            numCookiesWon = 3;
        else if (roll <= TWO)
            numCookiesWon = 2;
        else if (roll <= ONE)
            numCookiesWon = 1;
        else
            numCookiesWon = 0;

        String id = BotUtil.getMember(event).getId().asString();

        if (!lootboxTokenService.getAllByAccount(id).isEmpty())
            useToken = true;
        else if (cookieService.getAllCookiesForAccount(id) < COST)
            throw new CookieException("Lootbox costs 1 cookie. Not enough cookies to buy.");

        if (useToken) {
            lootboxTokenService.deleteLootBoxToken(id);
            BotUtil.sendMessage(event, "Spending token. Remaining tokens: " + lootboxTokenService.getAllByAccount(id).size());
        }
        else {
            cookieService.removeCookieOfType(id, CookieType.NORMAL);
            BotUtil.sendMessage(event, "Spending cookie. Remaining cookies: " + cookieService.getAllCookiesForAccount(id));
        }

        cookieService.generateCookie(id, CookieType.NORMAL, numCookiesWon);
        int newTotal = cookieService.getAllCookiesForAccount(id);

        BotUtil.sendMessage(event, "```You open the lootbox!\nContains..." + numCookiesWon + " cookies!\n" +
                BotUtil.getMember(event).getUsername() + " cookies: " + newTotal + "\n" +
                BotUtil.getMember(event).getUsername() + " tokens: " + lootboxTokenService.getAllByAccount(id).size() + "```");
    }

    @Autowired
    public void setCookieService(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Autowired
    public void setLootboxTokenService(LootboxTokenService lootboxTokenService) {
        this.lootboxTokenService = lootboxTokenService;
    }

    public static Integer getCOST() {
        return COST;
    }

    public static double getZERO() {
        return ZERO;
    }

    public static double getONE() {
        return ONE;
    }

    public static double getTWO() {
        return TWO;
    }

    public static double getTHREE() {
        return THREE;
    }

    public static double getFOUR() {
        return FOUR;
    }

    public static double getFIVE() {
        return FIVE;
    }

    public static double getSIX() {
        return SIX;
    }

    public static double getSEVEN() {
        return SEVEN;
    }

    private double roll() {
        int max = 100;
        int min = 0;
        return Math.random() * ((max - min) + 1) + min;
    }
}


