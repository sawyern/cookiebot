package sawyern.cookiebot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.models.entity.WorldBoss;
import sawyern.cookiebot.services.WorldBossService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedCookieCommand extends MessageCreateEventBotCommand {

    private final WorldBossService worldBossService;

    @Override
    public String getCommand() {
        return "wbfeed";
    }

    @Override
    public void execute(MessageCreateEvent event, List<String> args) throws CookieException {
        String discordId = getBotUtil().getMember(event).getId().asString();

        WorldBoss currentBoss = worldBossService.getCurrentBoss();
        worldBossService.feedCookie(currentBoss, discordId);
        if (worldBossService.rollExplosion()) {
            worldBossService.killAllWorldBosses();
        }
        worldBossService.awardCookies(currentBoss, discordId);
    }
}
