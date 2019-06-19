package sawyern.cookiebot.operations.worldboss;

import org.springframework.stereotype.Component;
import sawyern.cookiebot.constants.WorldBossType;

@Component(WorldBossType.WEEKLY)
public class WeeklyWorldBoss implements GenericWorldBoss {
    @Override
    public double getMultiplier() {
        return 2;
    }

    @Override
    public int getCookieCost() {
        return 1;
    }

    @Override
    public String getType() {
        return WorldBossType.WEEKLY;
    }

    @Override
    public double getExplosionProbability() {
        return 15d;
    }
}
