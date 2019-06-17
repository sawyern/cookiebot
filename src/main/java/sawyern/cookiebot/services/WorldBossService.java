package sawyern.cookiebot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.exception.RuntimeCookieException;
import sawyern.cookiebot.models.entity.WorldBoss;
import sawyern.cookiebot.models.entity.WorldBossHasCookie;
import sawyern.cookiebot.repository.WorldBossHasCookieRepository;
import sawyern.cookiebot.repository.WorldBossRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorldBossService {

    private final WorldBossRepository worldBossRepository;
    private final WorldBossHasCookieRepository worldBossHasCookieRepository;
    private final LootboxTokenService lootboxTokenService;

    @Scheduled(cron = "0 5 * * 5")
    public void spawnWorldBoss() {
        killAllWorldBosses();
        WorldBoss worldBoss = WorldBoss.builder()
                .isDead(false)
                .spawnTime(LocalDateTime.now())
                .hasCookies(new ArrayList<>())
                .build();
        worldBossRepository.save(worldBoss);
    }

    public void killAllWorldBosses() {
        List<WorldBoss> oldBosses = worldBossRepository.findByisDead(true);
        oldBosses.forEach(boss -> {
            boss.setDead(true);
            worldBossRepository.save(boss);
        });
    }

    public void awardCookies(WorldBoss worldBoss, String discordId) {
        worldBoss.getHasCookies().forEach(hasCookie -> {
            if (!hasCookie.getAccount().getDiscordId().equals(discordId)) {
                try {
                    lootboxTokenService.addLootboxToken(hasCookie.getAccount().getDiscordId(), (int)Math.ceil(hasCookie.getCookiesFed() * 2d));
                } catch (CookieException e) {
                    throw new RuntimeCookieException(e.getMessage());
                }
            }
        });
    }

    public void feedCookie(WorldBoss boss, String discordId) throws CookieException {
        try {
            WorldBossHasCookie hasCookie = worldBossHasCookieRepository.findByWorldBossIdAndAccountDiscordId(boss.getId(), discordId);
            hasCookie.setCookiesFed(hasCookie.getCookiesFed() + 1);
            worldBossHasCookieRepository.save(hasCookie);
        } catch (RuntimeCookieException e) {
            log.error(e.getMessage(), e);
            throw new CookieException(e.getMessage());
        }
    }

    public WorldBoss getCurrentBoss() throws CookieException {
        try {
            return worldBossRepository.findByisDead(false).stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeCookieException("All bosses are dead."));
        } catch (RuntimeCookieException e) {
            log.error(e.getMessage(), e);
            throw new CookieException(e.getMessage());
        }
    }

    public boolean rollExplosion() {
        int max = 100;
        int min = 1;
        double roll = Math.random() * ((max - min) + 1) + min;
        return roll < 50;
    }
}
