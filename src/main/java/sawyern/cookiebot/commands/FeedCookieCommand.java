package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.CookieType;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.models.entity.WorldBoss;
import sawyern.cookiebot.models.entity.WorldBossHasCookie;
import sawyern.cookiebot.services.AccountService;
import sawyern.cookiebot.services.CookieService;
import sawyern.cookiebot.services.WorldBossService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedCookieCommand extends MessageCreateEventBotCommand {

    private final WorldBossService worldBossService;
    public final AccountService accountService;
    public final CookieService cookieService;

    @Override
    public String getCommand() {
        return "wbfeed";
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        String discordId = getBotUtil().getMember(event).getId().asString();
        WorldBoss currentBoss = worldBossService.getCurrentBoss();

        cookieService.removeCookieOfType(discordId, CookieType.NORMAL);

        worldBossService.feedCookie(currentBoss, discordId);
        if (worldBossService.rollExplosion()) {
            worldBossService.killAllWorldBosses();
            List<WorldBossHasCookie> winners = worldBossService.awardCookies(currentBoss, discordId, currentBoss.getLastFed().getDiscordId());
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
            getBotUtil().sendMessage(event,"```The boss devours the cookie, but is not yet satisfied...```");
        }
        worldBossService.setLastFed(currentBoss, accountService.getAccount(discordId));
    }
}
