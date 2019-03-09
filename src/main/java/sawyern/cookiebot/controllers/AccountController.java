package sawyern.cookiebot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sawyern.cookiebot.models.dto.AccountDto;
import sawyern.cookiebot.bot.exception.CookieException;
import sawyern.cookiebot.services.AccountService;

import javax.transaction.Transactional;

@RestController
@RequestMapping(value = "/api/cookiebot/v1/accounts")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> registerNewUser(
            @RequestBody AccountDto accountDto
            ) {
        try {
            accountService.registerAccount(accountDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CookieException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }
    }

    @GetMapping
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("hi", HttpStatus.OK);
    }
}
