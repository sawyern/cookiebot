package sawyern.cookiebot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.models.entity.WorldBoss;
import sawyern.cookiebot.services.WorldBossService;

@RestController
@RequestMapping(value = "api/cookiebot/v1/worldboss")
public class WorldBossController {

    private WorldBossService worldBossService;

    @PostMapping("/spawn/daily")
    public void spawnWorldBoss() {
        worldBossService.spawnDailyWorldBoss();
    }

    @PostMapping("/spawn/weekly")
    public void spawnWeeklyWorldBoss() {
        worldBossService.spawnWeeklyWorldBoss();
    }

    @PostMapping("/kill")
    public void killAllWorldBosses() {
        worldBossService.killAllWorldBosses();
    }

    @GetMapping
    public ResponseEntity<WorldBoss> getWorldBoss() throws CookieException {
        return ResponseEntity.ok(worldBossService.getCurrentBoss());
    }

    @Autowired
    public void setWorldBossService(WorldBossService worldBossService) {
        this.worldBossService = worldBossService;
    }
}
