package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CookiesCommand extends GenericBotCommand {

    private final CookieService cookieService;

    @Override
    public String getCommand() {
        return "cookies";
    }

    @Override
    public List<Integer> allowedNumArgs() {
        return Arrays.asList(0, 1);
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
            discordId = BotUtil.getIdFromUser(event, getArgs().get(1));
            username = getArgs().get(1);
        }
        numCookies = cookieService.getAllCookiesForAccount(discordId);
        BotUtil.sendMessage(event, MessageFormat.format("{0} has {1} cookies.", username, numCookies));
    }
}
