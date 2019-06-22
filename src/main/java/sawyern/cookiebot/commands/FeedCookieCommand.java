package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.models.entity.WorldBoss;
import sawyern.cookiebot.models.entity.WorldBossHasCookie;
import sawyern.cookiebot.services.AccountService;
import sawyern.cookiebot.services.WorldBossService;

import java.util.List;

@Component
public class FeedCookieCommand extends MessageCreateEventBotCommand {

    private WorldBossService worldBossService;
    private AccountService accountService;

    @Override
    public String getCommand() {
        return "wbfeed";
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        String discordId = getBotUtil().getMember(event).getId().asString();
        WorldBoss currentBoss = worldBossService.getCurrentBoss();

        worldBossService.feedCookie(currentBoss, discordId);

        if (worldBossService.rollExplosion(currentBoss)) {
            worldBossService.killAllWorldBosses();

            String lastFed = currentBoss.getLastFed() != null ? currentBoss.getLastFed().getDiscordId() : "";

            List<WorldBossHasCookie> winners = worldBossService.awardCookies(currentBoss, discordId, lastFed);
            StringBuilder builder = new StringBuilder();
            builder.append("```");
            builder.append("The boss devours the cookie, and explodes!\n" +
                    "A shower of lootboxes rain on those who were brave enough to feed it...except for you.\n");
            winners.forEach(winner ->
                builder.append(winner.getAccount().getUsername())
                        .append(" fed ")
                        .append(winner.getCookiesFed())
                        .append(" cookies.\n")
            );
            builder.append("```");
            getBotUtil().sendMessage(event,builder.toString());
        } else {
            worldBossService.setLastFed(currentBoss, accountService.getAccount(discordId));
            getBotUtil().sendMessage(event,"```The boss devours the cookie, but is not yet satisfied...```");
        }
    }

    @Autowired
    public void setWorldBossService(WorldBossService worldBossService) {
        this.worldBossService = worldBossService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
