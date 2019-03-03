package sawyern.cookiebot.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/accounts")
public class AccountController {

    @GetMapping
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
