package sawyern.cookiebot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.HasCookie;

import java.util.List;

@Repository
public interface HasCookieRepository extends CrudRepository<HasCookie, String> {
    List<HasCookie> findByAccount(Account account);
}
