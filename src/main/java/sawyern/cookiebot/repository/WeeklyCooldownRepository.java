package sawyern.cookiebot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.WeeklyCooldown;

@Repository
public interface WeeklyCooldownRepository extends CrudRepository<WeeklyCooldown, String> {
    WeeklyCooldown findByAccount(Account account);
}
