package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.LootboxTokenService;
import sawyern.cookiebot.services.WeeklyCooldownService;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WeeklyTokenCommand extends MessageCreateEventBotCommand {

    private final WeeklyCooldownService weeklyCooldownService;
    private final LootboxTokenService lootboxTokenService;

    private static final int WEEKLY_TOKENS = 3;

    @Override
    public String getCommand() {
        return "weeklytokens";
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        String id = getBotUtil().getMember(event).getId().asString();
        if (weeklyCooldownService.isCooldown(id)) {
            weeklyCooldownService.putOnCooldown(id);
            lootboxTokenService.addLootboxToken(getBotUtil().getMember(event).getId().asString(), WEEKLY_TOKENS);
            getBotUtil().sendMessage(event, WEEKLY_TOKENS + " tokens have been awarded. Good luck!");
        } else {
            long remainingCd = weeklyCooldownService.getRemainingCooldownHours(id);
            NumberFormat format = new DecimalFormat("##.##");
            getBotUtil().sendMessage(event, MessageFormat.format("Weekly cooldown not reset. Time remaining: {0} hours", format.format(remainingCd)));
        }
    }
}
