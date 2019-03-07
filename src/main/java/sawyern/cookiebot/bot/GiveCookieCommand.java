package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.models.exception.CookieException;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;

import java.text.MessageFormat;

@Component
public class GiveCookieCommand extends GenericBotCommand {

    private CookieService cookieService;

    @Override
    public String getCommand() {
        return "givecookie";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        if (getArgs().size() != 3)
            throw new CookieException("Invalid arguments. !givecookie {num} {username}");
        GiveCookieDto giveCookieDto = new GiveCookieDto();
        giveCookieDto.setSenderId(BotUtil.getMember(event).getId().asString());
        giveCookieDto.setRecieverId(BotUtil.getIdFromUser(event, getArgs().get(2)));

        try {
            giveCookieDto.setNumCookies(Integer.parseInt(getArgs().get(1)));
        } catch (NumberFormatException e) {
            throw new CookieException("Invalid argument.");
        }

        cookieService.giveCookieTo(giveCookieDto);

        String senderUser = BotUtil.getMember(event).getUsername();
        String recieverUser = getArgs().get(2);
        int senderTotal = cookieService.getAllCookiesForAccount(giveCookieDto.getSenderId());
        int recieverTotal = cookieService.getAllCookiesForAccount(giveCookieDto.getRecieverId());

        sendMessage(event, MessageFormat.format(
                "Successfully transferred cookies. {0}: {1}, {2}: {3}",
                senderUser,
                String.valueOf(senderTotal),
                recieverUser,
                String.valueOf(recieverTotal))
        );
    }

    @Autowired
    public void setCookieService(CookieService cookieService) {
        this.cookieService = cookieService;
    }
}
