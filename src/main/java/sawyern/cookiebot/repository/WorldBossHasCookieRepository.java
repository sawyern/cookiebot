package sawyern.cookiebot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sawyern.cookiebot.models.entity.WorldBossHasCookie;

import java.util.List;

@Repository
public interface WorldBossHasCookieRepository extends CrudRepository<WorldBossHasCookie, String> {
    WorldBossHasCookie findByWorldBossIdAndAccountDiscordId(String worldBossId, String discordId);
    List<WorldBossHasCookie> findByWorldBossId(String worldBossId);
}
