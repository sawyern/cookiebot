package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GiveCookieCommand extends GenericBotCommand {

    private final CookieService cookieService;

    @Override
    public String getCommand() {
        return "givecookie";
    }

    @Override
    public List<Integer> allowedNumArgs() {
        return Arrays.asList(1, 2);
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        GiveCookieDto giveCookieDto = new GiveCookieDto();
        giveCookieDto.setSenderId(BotUtil.getMember(event).getId().asString());

        String senderUser = BotUtil.getMember(event).getUsername();
        String recieverUser = null;

        if (getArgs().size() == 2) {
            recieverUser = getArgs().get(1);
            if (recieverUser.equalsIgnoreCase(BotUtil.getMember(event).getUsername()))
                throw new CookieException("Can't give cookies to yourself.");

            // first argument is username
            giveCookieDto.setRecieverId(BotUtil.getIdFromUser(event, getArgs().get(1)));
            // give one cookie if not specified
            giveCookieDto.setNumCookies(1);
        }
        else if (getArgs().size() == 3) {
            recieverUser = getArgs().get(2);
            if (recieverUser.equalsIgnoreCase(BotUtil.getMember(event).getUsername()))
                throw new CookieException("Can't give cookies to yourself.");

            // second argument is username
            giveCookieDto.setRecieverId(BotUtil.getIdFromUser(event, getArgs().get(2)));
            // first argument is numCookies
            giveCookieDto.setNumCookies(BotUtil.parseIntArgument(getArgs().get(1)));
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
