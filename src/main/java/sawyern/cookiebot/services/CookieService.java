package sawyern.cookiebot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.constants.CookieType;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.Cookie;
import sawyern.cookiebot.models.entity.HasCookie;
import sawyern.cookiebot.bot.exception.CookieException;
import sawyern.cookiebot.repository.CookieRepository;
import sawyern.cookiebot.repository.HasCookieRepository;

import javax.transaction.Transactional;
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

    @Transactional
    public void giveCookieTo(GiveCookieDto giveCookieDto) throws CookieException {
        if (giveCookieDto.getNumCookies() <= 0)
            throw new CookieException("Invalid number of cookies", HttpStatus.BAD_REQUEST);
        int cookies = getAllCookiesForAccount(giveCookieDto.getSenderId());
        if (cookies < giveCookieDto.getNumCookies())
            throw new CookieException("Not enough cookies to give", HttpStatus.BAD_REQUEST);

        for (int i = 0; i < giveCookieDto.getNumCookies(); i++) {
            removeCookieOfType(giveCookieDto.getSenderId(), CookieType.NORMAL);
            generateCookie(giveCookieDto.getRecieverId(), CookieType.NORMAL);
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

    @Transactional
    public void removeCookieOfType(String discordId, String type) throws CookieException {
        Account account = accountService.getAccount(discordId);
        List<HasCookie> cookies = hasCookieRepository.findByAccount(account);
        cookies = cookies.stream().filter(hasCookie -> hasCookie.getCookie().getType().equals(type)).collect(Collectors.toList());
        HasCookie hasCookie = cookies.stream().findFirst().orElseThrow(() -> new CookieException("No cookies to remove", HttpStatus.NOT_FOUND));
        Cookie cookie = hasCookie.getCookie();
        try {
            hasCookieRepository.delete(hasCookie);
            cookieRepository.deleteById(cookie.getId());
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

    @Transactional
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
