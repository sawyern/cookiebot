package sawyern.cookiebot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sawyern.cookiebot.constants.CookieType;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.models.entity.Account;
import sawyern.cookiebot.models.entity.Cookie;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.repository.CookieRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CookieService {

    private final CookieRepository cookieRepository;
    private final AccountService accountService;

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

    public int getAllCookiesForAccount(String discordId) {
        Collection<Cookie> cookies = cookieRepository.findByAccountDiscordId(discordId);
        return cookies.size();
    }

    public void removeCookieOfType(String discordId, String type) throws CookieException {
        Collection<Cookie> cookies = cookieRepository.findByAccountDiscordId(discordId);
        cookies = cookies.stream()
                .filter(cookie -> cookie.getType().equals(type))
                .collect(Collectors.toList());

        if (cookies.isEmpty())
            throw new CookieException("No cookies to remove.");

        try {
            cookieRepository.delete(cookies.stream().findFirst().orElseThrow(() -> new CookieException("Cookie not found")));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CookieException("Failed to delete cookie");
        }
    }

    public void generateCookie(String discordId, String type, int num) throws CookieException {
        for (int i = 0; i < num; i++) {
            generateCookie(discordId, type);
        }
    }

    public Cookie generateCookie(String discordId) throws CookieException {
        return generateCookie(discordId, CookieType.NORMAL);
    }

    public Cookie generateCookie(String discordId, String type) throws CookieException {
        Account account = accountService.getAccount(discordId);
        Cookie cookie = new Cookie(account, type);
        try {
            cookieRepository.save(cookie);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CookieException("Error saving cookie");
        }
        return cookie;
    }
}
