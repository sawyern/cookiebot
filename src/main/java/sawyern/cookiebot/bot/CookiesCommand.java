package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.exception.CookieException;
import sawyern.cookiebot.services.CookieService;

import java.text.MessageFormat;

@Component
public class CookiesCommand extends GenericBotCommand {

    private CookieService cookieService;

    public CookiesCommand(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Override
    public String getCommand() {
        return "cookies";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        String discordId = "";
        int numCookies;
        String username = "";

        if (getArgs().size() == 1) {
            Member member = event.getMember().orElseThrow(() -> new CookieException("Error getting member."));
            discordId = member.getId().asString();
            username = member.getUsername();
        }
        else if (getArgs().size() == 2) {
            User user = event.getClient().getUsers()
                    .toStream()
                    .filter(u -> u.getUsername().equalsIgnoreCase(getArgs().get(1)))
                    .findFirst().orElseThrow(() -> new CookieException("Error getting user " + getArgs().get(1)));

            discordId = user.getId().asString();
            username = user.getUsername();
        }
        numCookies = cookieService.getAllCookiesForAccount(discordId);
        sendMessage(event, MessageFormat.format("{0} has {1} cookies.", username, numCookies));
    }
}