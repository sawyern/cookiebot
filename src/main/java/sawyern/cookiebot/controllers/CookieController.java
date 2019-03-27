package sawyern.cookiebot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import sawyern.cookiebot.constants.CookieType;
import sawyern.cookiebot.models.dto.CookieDto;
import sawyern.cookiebot.models.dto.GiveCookieDto;
import sawyern.cookiebot.exception.CookieException;
import sawyern.cookiebot.services.CookieService;

@RestController
@RequestMapping(value = "/cookies")
@RequiredArgsConstructor
public class CookieController implements GenericController {

    private final CookieService cookieService;

    @GetMapping
    public ResponseEntity<CookieDto> getCookies(
            @RequestParam(name = "id") String discordId
    ) {
        try {
            CookieDto dto = CookieDto.builder()
                    .discordId(discordId)
                    .numCookies(cookieService.getAllCookiesForAccount(discordId))
                    .build();
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
