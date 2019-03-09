package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.dto.AccountDto;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.AccountService;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.util.BotUtil;

@Component
public class RegisterCommand extends GenericBotCommand {

    private AccountService accountService;
    private CookieService cookieService;

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        Member member = BotUtil.getMember(event);
        AccountDto accountDto = new AccountDto();
        accountDto.setDiscordId(member.getId().asString());
        accountDto.setUsername(member.getUsername());
        accountService.registerAccount(accountDto);

        for (int i = 0; i < 10; i++)
            cookieService.generateCookie(accountDto.getDiscordId());
        BotUtil.sendMessage(event, "Successfully registered " + accountDto.getUsername());
    }

    @Override
    public String getCommand() {
        return "register";
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setCookieService(CookieService cookieService) {
        this.cookieService = cookieService;
    }
}
