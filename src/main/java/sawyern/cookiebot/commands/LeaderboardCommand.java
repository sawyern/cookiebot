package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.AccountService;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;
import sawyern.cookiebot.util.MapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LeaderboardCommand extends MessageCreateEventBotCommand {

    private final CookieService cookieService;
    private final AccountService accountService;

    @Override
    public String getCommand() {
        return "leaderboard";
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
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
}
