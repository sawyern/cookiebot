package sawyern.cookiebot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.exception.RuntimeCookieException;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.WorldBoss;
import sawyern.cookiebot.models.entity.WorldBossHasCookie;
import sawyern.cookiebot.repository.WorldBossHasCookieRepository;
import sawyern.cookiebot.repository.WorldBossRepository;
import sawyern.cookiebot.util.BotUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WorldBossService {

    private WorldBossRepository worldBossRepository;
    private WorldBossHasCookieRepository worldBossHasCookieRepository;
    private LootboxTokenService lootboxTokenService;
    private AccountService accountService;
    private BotUtil botUtil;

    @Scheduled(cron = "0 0 17 ? * MON-SAT *")
    public void spawnWorldBoss() {
        log.info("Spawning world boss.");
        killAllWorldBosses();
        WorldBoss worldBoss = WorldBoss.builder()
                .isDead(false)
                .spawnTime(LocalDateTime.now())
                .hasCookies(new ArrayList<>())
                .build();
        worldBossRepository.save(worldBoss);
        botUtil.sendMessage("@here ```World Boss Spawned!!! and hungry...```");
    }

    public void killAllWorldBosses() {
        List<WorldBoss> oldBosses = worldBossRepository.findByisDead(false);
        oldBosses.forEach(boss -> {
            boss.setDead(true);
            worldBossRepository.save(boss);
        });
    }

    public List<WorldBossHasCookie> awardCookies(WorldBoss worldBoss, String loser, String winner) {
        List<WorldBossHasCookie> winners = new ArrayList<>();
        worldBossHasCookieRepository.findByWorldBossId(worldBoss.getId()).forEach(hasCookie -> {
            if (!hasCookie.getAccount().getDiscordId().equals(loser)) {
                try {
                    if (hasCookie.getAccount().getDiscordId().equals(winner))
                        lootboxTokenService.addLootboxToken(hasCookie.getAccount().getDiscordId(), (int)Math.ceil(hasCookie.getCookiesFed() * 1.5d));
                    lootboxTokenService.addLootboxToken(hasCookie.getAccount().getDiscordId(), (int)Math.ceil(hasCookie.getCookiesFed() * 2d));
                    winners.add(hasCookie);
                } catch (CookieException e) {
                    throw new RuntimeCookieException(e.getMessage());
                }
            }
        });
        return winners;
    }

    public void feedCookie(WorldBoss boss, String discordId) throws CookieException {
        try {
            WorldBossHasCookie hasCookie = worldBossHasCookieRepository.findByWorldBossIdAndAccountDiscordId(boss.getId(), discordId);
            if (hasCookie == null) {
                hasCookie = WorldBossHasCookie.builder()
                        .account(accountService.getAccount(discordId))
                        .cookiesFed(0)
                        .worldBoss(boss)
                        .build();
            }

            hasCookie.setCookiesFed(hasCookie.getCookiesFed() + 1);
            worldBossHasCookieRepository.save(hasCookie);
        } catch (Exception e) {
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

    public void setLastFed(WorldBoss worldBoss, Account account) {
        worldBoss.setLastFed(account);
        worldBossRepository.save(worldBoss);
    }

    public boolean rollExplosion() {
        int max = 100;
        int min = 1;
        double roll = Math.random() * ((max - min) + 1) + min;
        return roll <= 33;
    }

    @Autowired
    public void setWorldBossRepository(WorldBossRepository worldBossRepository) {
        this.worldBossRepository = worldBossRepository;
    }

    @Autowired
    public void setWorldBossHasCookieRepository(WorldBossHasCookieRepository worldBossHasCookieRepository) {
        this.worldBossHasCookieRepository = worldBossHasCookieRepository;
    }

    @Autowired
    public void setLootboxTokenService(LootboxTokenService lootboxTokenService) {
        this.lootboxTokenService = lootboxTokenService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setBotUtil(BotUtil botUtil) {
        this.botUtil = botUtil;
    }
}
