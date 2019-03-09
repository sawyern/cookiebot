package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.bot.exception.CookieException;
import sawyern.cookiebot.services.AccountService;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;
import sawyern.cookiebot.util.MapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LeaderboardCommand extends GenericBotCommand {

    private CookieService cookieService;
    private AccountService accountService;

    @Override
    public String getCommand() {
        return "leaderboard";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        StringBuilder leaderboard = new StringBuilder("```");
        Map<String, Integer> leaderboardMap = new HashMap<>();

        List<Account> accounts = accountService.findAllAccount();

        for (Account account : accounts) {
            int cookies = cookieService.getAllCookiesForAccount(account.getDiscordId());
            leaderboardMap.put(account.getUsername(), cookies);
        }

        leaderboardMap = MapUtil.sortByValue(leaderboardMap);
        for (Map.Entry<String, Integer> entry : leaderboardMap.entrySet()) {
            leaderboard.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }

        leaderboard.append("```");
        BotUtil.sendMessage(event, leaderboard.toString());
    }

    @Autowired
    public void setCookieService(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Autowired

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
