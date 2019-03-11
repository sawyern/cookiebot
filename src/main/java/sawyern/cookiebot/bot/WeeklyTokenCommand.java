package sawyern.cookiebot.bot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.LootboxTokenService;
import sawyern.cookiebot.services.WeeklyCooldownService;
import sawyern.cookiebot.util.BotUtil;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;

@Component
public class WeeklyTokenCommand extends GenericBotCommand {

    private WeeklyCooldownService weeklyCooldownService;
    private LootboxTokenService lootboxTokenService;

    private static final int WEEKLY_TOKENS = 3;

    @Override
    public String getCommand() {
        return "weeklytokens";
    }

    @Override
    public void execute(MessageCreateEvent event) throws CookieException {
        String id = BotUtil.getMember(event).getId().asString();
        if (weeklyCooldownService.isCooldown(id)) {
            weeklyCooldownService.putOnCooldown(id);
            lootboxTokenService.addLootboxToken(BotUtil.getMember(event).getId().asString(), WEEKLY_TOKENS);
            BotUtil.sendMessage(event, WEEKLY_TOKENS + " tokens have been awarded. Good luck!");
        } else {
            long remainingCd = weeklyCooldownService.getRemainingCooldownHours(id);
            NumberFormat format = new DecimalFormat("##.##");
            BotUtil.sendMessage(event, MessageFormat.format("Weekly cooldown not reset. Time remaining: {0} hours", format.format(remainingCd)));
        }
    }

    @Autowired
    public void setLootboxTokenService(LootboxTokenService lootboxTokenService) {
        this.lootboxTokenService = lootboxTokenService;
    }

    @Autowired
    public void setWeeklyCooldownService(WeeklyCooldownService weeklyCooldownService) {
        this.weeklyCooldownService = weeklyCooldownService;
    }
}
