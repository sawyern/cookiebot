package sawyern.cookiebot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.constants.CookieType;
import sawyern.cookiebot.constants.WorldBossType;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.exception.RuntimeCookieException;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.WorldBoss;
import sawyern.cookiebot.models.entity.WorldBossHasCookie;
import sawyern.cookiebot.operations.worldboss.GenericWorldBoss;
import sawyern.cookiebot.repository.WorldBossHasCookieRepository;
import sawyern.cookiebot.repository.WorldBossRepository;
import sawyern.cookiebot.util.BotUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WorldBossService {

    private WorldBossRepository worldBossRepository;
    private WorldBossHasCookieRepository worldBossHasCookieRepository;
    private LootboxTokenService lootboxTokenService;
    private AccountService accountService;
    private BotUtil botUtil;
    private CookieService cookieService;

    private Map<String, GenericWorldBoss> worldBossMap;

    @Scheduled(cron = "0 0 3 * * SUN-TUE")
    public void spawnDailyWorldBoss() {
        log.info("Spawning daily world boss.");
        killAllWorldBosses();
        WorldBoss worldBoss = worldBossMap.get(WorldBossType.DAILY).create();
        worldBossRepository.save(worldBoss);
        botUtil.sendMessage("@here ```Daily World Boss Spawned!!! and hungry...```");
    }

    @Scheduled(cron = "0 0 3 * * MON")
    public void spawnWeeklyWorldBoss() {
        log.info("Spawning weekly world boss.");
        killAllWorldBosses();
        WorldBoss worldBoss = worldBossMap.get(WorldBossType.WEEKLY).create();
        worldBossRepository.save(worldBoss);
        botUtil.sendMessage("@here ```Weekly World Boss Spawned!!! and hungry...```");
    }

    @Scheduled(cron = "0 0 16 * * *")
    public void despawnBoss() throws CookieException {
        botUtil.sendMessage("```Despawning world boss...````");
        WorldBoss currentBoss = getCurrentBoss();
        WorldBossHasCookie hasCookie = worldBossHasCookieRepository.findByWorldBossId(currentBoss.getId())
                .stream().filter(hc -> hc.getAccount().getDiscordId().equals(currentBoss.getLastFed().getDiscordId()))
                .findFirst()
                .orElseThrow(() -> new CookieException("No last feeder."));
        final double multiplier = worldBossMap.get(currentBoss.getType()).getMultiplier();

        lootboxTokenService.addLootboxToken(hasCookie.getAccount().getDiscordId(), (int)Math.ceil(hasCookie.getCookiesFed() * multiplier));

        killAllWorldBosses();
        botUtil.sendMessage("@here ```World Boss leaves... dissatisfied...but leaves behind a treat for the last feeder.```");
    }

    public void killAllWorldBosses() {
        List<WorldBoss> oldBosses = worldBossRepository.findByIsDead(false);
        oldBosses.forEach(boss -> {
            boss.setDead(true);
            worldBossRepository.save(boss);
        });
    }

    public List<WorldBossHasCookie> awardCookies(WorldBoss worldBoss, String loser, String winner) {
        List<WorldBossHasCookie> winners = new ArrayList<>();

        final double multiplier = worldBossMap.get(worldBoss.getType()).getMultiplier();

        worldBossHasCookieRepository.findByWorldBossId(worldBoss.getId()).forEach(hasCookie -> {
            if (!hasCookie.getAccount().getDiscordId().equals(loser)) {
                try {
                    if (hasCookie.getAccount().getDiscordId().equals(winner))
                        lootboxTokenService.addLootboxToken(hasCookie.getAccount().getDiscordId(), (int)Math.ceil(hasCookie.getCookiesFed() * 1.5d));
                    lootboxTokenService.addLootboxToken(hasCookie.getAccount().getDiscordId(), (int)Math.ceil(hasCookie.getCookiesFed() * multiplier));
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
            for (int i = 0; i < worldBossMap.get(boss.getType()).getCookieCost(); i++) {
                cookieService.removeCookieOfType(discordId, CookieType.NORMAL);
            }

            WorldBossHasCookie hasCookie = worldBossHasCookieRepository.findByWorldBossIdAndAccountDiscordId(boss.getId(), discordId);
            if (hasCookie == null) {
                hasCookie = WorldBossHasCookie.builder()
                        .account(accountService.getAccount(discordId))
                        .cookiesFed(0)
                        .worldBoss(boss)
                        .build();
            }

            hasCookie.setCookiesFed(hasCookie.getCookiesFed() + worldBossMap.get(boss.getType()).getCookieCost());
            worldBossHasCookieRepository.save(hasCookie);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CookieException(e.getMessage());
        }
    }

    public WorldBoss getCurrentBoss() throws CookieException {
        try {
            return worldBossRepository.findByIsDead(false).stream()
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

    public boolean rollExplosion(WorldBoss worldBoss) {
        int max = 100;
        int min = 1;
        double roll = Math.random() * ((max - min) + 1) + min;
        return roll <= worldBossMap.get(worldBoss.getType()).getExplosionProbability();
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

    @Autowired
    public void setWorldBossMap(Map<String, GenericWorldBoss> worldBossMap) {
        this.worldBossMap = worldBossMap;
    }

    @Autowired
    public void setCookieService(CookieService cookieService) {
        this.cookieService = cookieService;
    }
}
