package sawyern.cookiebot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sawyern.cookiebot.models.entity.WorldBoss;

import java.util.List;

@Repository
public interface WorldBossRepository extends CrudRepository<WorldBoss, String> {
    List<WorldBoss> findByIsDead(boolean isDead);
}
