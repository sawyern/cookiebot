package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.exception.CookieException;
import sawyern.cookiebot.services.AccountService;

@Component
public class RegisterCommand extends GenericBotCommand {

    private AccountService accountService;

    @Autowired
    public RegisterCommand (AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        Member member = event.getMember().orElseThrow(CookieException::new);
        accountService.registerAccount(member.getId().asString());
    }

    @Override
    public String getCommand() {
        return "register";
    }
}
