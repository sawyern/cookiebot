package sawyern.cookiebot.repository;

import org.springframework.data.repository.CrudRepository;
import sawyern.cookiebot.models.entity.Cookie;

import java.util.Collection;

public interface CookieRepository extends CrudRepository<Cookie, String> {
    Collection<Cookie> findByAccountDiscordId(String discordId);
}
