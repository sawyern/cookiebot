package sawyern.cookiebot.controllers;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class WorldBossController {

    private final WorldBossService worldBossService;

    @GetMapping("/spawn")
    public void spawnWorldBoss() {
        worldBossService.spawnWorldBoss();
    }

    @PostMapping("/kill")
    public void killAllWorldBosses() {
        worldBossService.killAllWorldBosses();
    }

    @GetMapping
    public ResponseEntity<WorldBoss> getWorldBoss() throws CookieException {
        return ResponseEntity.ok(worldBossService.getCurrentBoss());
    }
}
