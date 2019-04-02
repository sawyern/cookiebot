package sawyern.cookiebot.bot;

import com.google.common.collect.Sets;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GiveCookieCommand extends GenericBotCommand {

    private final CookieService cookieService;

    @Override
    public String getCommand() {
        return "givecookie";
    }

    @Override
    public Set<Integer> getAllowedNumArgs() {
        return Sets.newHashSet(1, 2);
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        GiveCookieDto giveCookieDto = new GiveCookieDto();
        giveCookieDto.setSenderId(BotUtil.getMember(event).getId().asString());

        String senderUser = BotUtil.getMember(event).getUsername();
        String recieverUser = null;

        if (args.size() == 1) {
            recieverUser = args.get(0);
            if (recieverUser.equalsIgnoreCase(BotUtil.getMember(event).getUsername()))
                throw new CookieException("Can't give cookies to yourself.");

            // first argument is username
            giveCookieDto.setRecieverId(BotUtil.getIdFromUser(event, args.get(0)));
            // give one cookie if not specified
            giveCookieDto.setNumCookies(1);
        }
        else if (args.size() == 2) {
            recieverUser = args.get(1);
            if (recieverUser.equalsIgnoreCase(BotUtil.getMember(event).getUsername()))
                throw new CookieException("Can't give cookies to yourself.");

            // second argument is username
            giveCookieDto.setRecieverId(BotUtil.getIdFromUser(event, args.get(1)));
            // first argument is numCookies
            giveCookieDto.setNumCookies(BotUtil.parsePositiveIntArgument(args.get(0)));
        }

        cookieService.giveCookieTo(giveCookieDto);

        int senderTotal = cookieService.getAllCookiesForAccount(giveCookieDto.getSenderId());
        int recieverTotal = cookieService.getAllCookiesForAccount(giveCookieDto.getRecieverId());

        BotUtil.sendMessage(event, MessageFormat.format(
                "Successfully transferred cookies. {0}: {1}, {2}: {3}",
                senderUser,
                String.valueOf(senderTotal),
                recieverUser,
                String.valueOf(recieverTotal))
        );
    }
}
