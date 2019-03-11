package sawyern.cookiebot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.LootboxToken;

import java.util.List;

@Repository
public interface LootboxTokenRepository extends CrudRepository<LootboxToken, String> {
    List<LootboxToken> findByAccount(Account account);
}
