package sawyern.cookiebot.commands;

import com.google.common.collect.Sets;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.CommandConstants;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;

import java.text.MessageFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CookiesCommand extends MessageCreateEventBotCommand {

    private final CookieService cookieService;

    @Override
    public String getCommand() {
        return CommandConstants.CommandName.COOKIES;
    }

    @Override
    public Set<Integer> getAllowedNumArgs() {
        return Sets.newHashSet(0, 1);
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        String discordId = "";
        int numCookies;
        String username = "";

        if (args.isEmpty()) {
            Member member = event.getMember().orElseThrow(() -> new CookieException("Error getting member."));
            discordId = member.getId().asString();
            username = member.getUsername();
        }
        else if (args.size() == 1) {
            discordId = BotUtil.getIdFromUser(event, args.get(0));
            username = args.get(0);
        }

        numCookies = cookieService.getAllCookiesForAccount(discordId);
        BotUtil.sendMessage(event, MessageFormat.format("{0} has {1} cookies.", username, numCookies));
    }
}
