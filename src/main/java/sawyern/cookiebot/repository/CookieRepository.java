package sawyern.cookiebot.repository;

import org.springframework.data.repository.CrudRepository;
import sawyern.cookiebot.models.entity.Cookie;

public interface CookieRepository extends CrudRepository<Cookie, String> {
}
