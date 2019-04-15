package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.models.dto.AccountDto;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.AccountService;
import sawyern.cookiebot.services.CookieService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegisterCommand extends MessageCreateEventBotCommand {

    private final AccountService accountService;
    private final CookieService cookieService;

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        Member member = botUtil.getMember(event);
        AccountDto accountDto = AccountDto.builder()
                .discordId(member.getId().asString())
                .username(member.getUsername())
                .build();
        accountService.registerAccount(accountDto);

        for (int i = 0; i < 10; i++)
            cookieService.generateCookie(accountDto.getDiscordId());
        botUtil.sendMessage(event, "Successfully registered " + accountDto.getUsername());
    }

    @Override
    public String getCommand() {
        return "register";
    }
}
