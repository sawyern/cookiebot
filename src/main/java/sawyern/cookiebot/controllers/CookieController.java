package sawyern.cookiebot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import sawyern.cookiebot.constants.CookieType;
import sawyern.cookiebot.models.dto.CookieDto;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.bot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;

import javax.transaction.Transactional;

@RestController
@RequestMapping(value = "api/cookiebot/v1/cookies")
public class CookieController {

    private CookieService cookieService;

    @Autowired
    public CookieController(
            CookieService cookieService
    ) {
        this.cookieService = cookieService;
    }

    @GetMapping
    public ResponseEntity<CookieDto> getCookies(
            @RequestParam(name = "id") String discordId
    ) {
        try {
            CookieDto dto = new CookieDto();
            dto.setDiscordId(discordId);
            dto.setNumCookies(cookieService.getAllCookiesForAccount(discordId));
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (CookieException e) {
            return new ResponseEntity<>(e.getHttpStatus());
        }
    }

    @PostMapping(value = "/give")
    public ResponseEntity<String> giveCookieTo(
            @RequestBody GiveCookieDto giveCookieDto
            ) {
        try {
            cookieService.giveCookieTo(giveCookieDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CookieException e) {
            return new ResponseEntity<>(e.getHttpStatus());
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> addCookie(
            @RequestParam(name = "id") String discordId,
            @RequestParam(required = false, defaultValue = "1") int numCookies
    ) {
        try {
            for (int i = 0; i < numCookies; i++)
                cookieService.generateCookie(discordId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CookieException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<String> removeCookie(
            @RequestParam(name = "id") String discordId
    ) {
        try {
            cookieService.removeCookieOfType(discordId, CookieType.NORMAL);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CookieException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }
    }
}
