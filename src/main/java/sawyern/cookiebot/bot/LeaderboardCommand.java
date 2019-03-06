package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.exception.CookieException;
import sawyern.cookiebot.services.AccountService;
import sawyern.cookiebot.services.CookieService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> leaderboard = new ArrayList<>();
        List<String> userIds = event.getClient().getUsers()
                .toStream()
                .map(user -> user.getId().toString())
                .collect(Collectors.toList());
        leaderboard.add("```");
        for (String id : userIds) {
            try {
                Account account = accountService.getAccount(id);
                int cookies = cookieService.getAllCookiesForAccount(id);
                leaderboard.add(account.getUsername() + ": " + cookies + "\n");
            } catch (Exception e) {
                continue;
            }
        }
        leaderboard.add("```");
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
