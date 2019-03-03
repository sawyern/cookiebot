package sawyern.cookiebot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sawyern.cookiebot.models.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

}
