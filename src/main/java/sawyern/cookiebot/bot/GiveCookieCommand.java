package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.models.exception.CookieException;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;

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
        giveCookieDto.setNumCookies(Integer.parseInt(getArgs().get(1)));
        cookieService.giveCookieTo(giveCookieDto);
    }

    @Autowired
    public void setCookieService(CookieService cookieService) {
        this.cookieService = cookieService;
    }
}
