package sawyern.cookiebot.operations.worldboss;

import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.WorldBossType;

@Component(WorldBossType.DAILY)
public class DailyWorldBoss implements GenericWorldBoss {
    @Override
    public double getMultiplier() {
        return 2;
    }

    @Override
    public int getCookieCost() {
        return 2;
    }

    @Override
    public String getType() {
        return WorldBossType.DAILY;
    }

    @Override
    public double getExplosionProbability() {
        return 33d;
    }
}
