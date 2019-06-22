package sawyern.cookiebot.operations.worldboss;

import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.WorldBossType;

@Component(WorldBossType.WEEKLY)
public class WeeklyWorldBoss implements GenericWorldBoss {
    @Override
    public double getMultiplier() {
        return 3;
    }

    @Override
    public int getCookieCost() {
        return 2;
    }

    @Override
    public String getType() {
        return WorldBossType.WEEKLY;
    }

    @Override
    public double getExplosionProbability() {
        return 20d;
    }
}
