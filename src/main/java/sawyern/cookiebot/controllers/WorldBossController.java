package sawyern.cookiebot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sawyern.cookiebot.services.WorldBossService;

@Controller
@RequestMapping(value = "api/cookiebot/v1/worldboss")
@RequiredArgsConstructor
public class WorldBossController {

    private final WorldBossService worldBossService;

    @RequestMapping("/spawn")
    public void spawnWorldBoss() {
        worldBossService.spawnWorldBoss();
    }

    @RequestMapping("/kill")
    public void killAllWorldBosses() {
        worldBossService.killAllWorldBosses();
    }
}
