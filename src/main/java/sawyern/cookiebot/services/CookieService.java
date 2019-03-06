package sawyern.cookiebot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.constants.CookieType;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.Cookie;
import sawyern.cookiebot.models.entity.HasCookie;
import sawyern.cookiebot.models.exception.CookieException;
import sawyern.cookiebot.repository.CookieRepository;
import sawyern.cookiebot.repository.HasCookieRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CookieService {

    private HasCookieRepository hasCookieRepository;
    private CookieRepository cookieRepository;
    private AccountService accountService;

    private static Logger LOGGER = LoggerFactory.getLogger(CookieService.class);

    @Autowired
    public CookieService(
            HasCookieRepository hasCookieRepository,
            CookieRepository cookieRepository,
            AccountService accountService
    ) {
        this.hasCookieRepository = hasCookieRepository;
        this.cookieRepository = cookieRepository;
        this.accountService = accountService;
    }

    public void giveCookieTo(String discordId, int numCookies, String recipientId) throws CookieException {
        try {
            if (numCookies <= 0)
                throw new CookieException("Invalid number of cookies", HttpStatus.BAD_REQUEST);
            int cookies = getAllCookiesForAccount(discordId);
            if (cookies < numCookies)
                throw new CookieException("Not enough cookies to give", HttpStatus.BAD_REQUEST);

            for (int i = 0; i < numCookies; i++) {
                removeCookieOfType(discordId, CookieType.NORMAL);
                generateCookie(recipientId, CookieType.NORMAL);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException("Error transferring cookies", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public int getAllCookiesForAccount(String discordId) throws CookieException {
        Account account = accountService.getAccount(discordId);
        List<HasCookie> hasCookies = hasCookieRepository.findByAccount(account);
        return hasCookies.size();
    }

    public Cookie getCookie(String id) throws CookieException {
        return cookieRepository.findById(id).orElseThrow(CookieException::new);
    }

    public void removeCookieOfType(String discordId, String type) throws CookieException {
        Account account = accountService.getAccount(discordId);
        List<HasCookie> cookies = hasCookieRepository.findByAccount(account);
        cookies = cookies.stream().filter(hasCookie -> hasCookie.getCookie().getType().equals(type)).collect(Collectors.toList());
        HasCookie hasCookie = cookies.stream().findFirst().orElseThrow(() -> new CookieException("No cookies to remove", HttpStatus.NOT_FOUND));
        Cookie cookie = hasCookie.getCookie();
        try {
            cookieRepository.delete(cookie);
            hasCookieRepository.delete(hasCookie);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException("Failed to delete cookie");
        }
    }

    public void deleteCookie(String id) throws CookieException {
        try {
            cookieRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException(HttpStatus.NOT_FOUND);
        }
    }

    public Cookie createCookie(String type) throws CookieException {
        return null;
    }

    public Cookie generateCookie(String discordId) throws CookieException {
        return generateCookie(discordId, CookieType.NORMAL);
    }

    public Cookie generateCookie(String discordId, String type) throws CookieException {
        Cookie cookie = new Cookie(type);
        Account account = accountService.getAccount(discordId);
        HasCookie hasCookie = new HasCookie(account, cookie);
        try {
            cookieRepository.save(cookie);
            hasCookieRepository.save(hasCookie);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CookieException("Error saving cookie");
        }
        return cookie;
    }
}
