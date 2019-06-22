package sawyern.cookiebot.operations.worldboss;

import sawyern.cookiebot.models.entity.WorldBoss;

import java.time.LocalDateTime;

public interface GenericWorldBoss {

    default WorldBoss create() {
        return WorldBoss.builder()
                .isDead(false)
                .spawnTime(LocalDateTime.now())
                .type(getType())
                .build();
    }

    double getMultiplier();
    int getCookieCost();
    String getType();
    double getExplosionProbability();
}
