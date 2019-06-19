package sawyern.cookiebot.operations.worldboss;

import sawyern.cookiebot.models.entity.WorldBoss;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface GenericWorldBoss {

    default WorldBoss create() {
        return WorldBoss.builder()
                .isDead(false)
                .spawnTime(LocalDateTime.now())
                .hasCookies(new ArrayList<>())
                .type(getType())
                .build();
    }

    double getMultiplier();
    int getCookieCost();
    String getType();
    double getExplosionProbability();
}
