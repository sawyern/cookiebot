package sawyern.cookiebot.controllers;

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping;
import sawyern.cookiebot.models.Account
import sawyern.cookiebot.services.AccountService

@Controller
@RequestMapping("/api/cookiebot/v1/accounts")
class AccountController (
        private val accountService: AccountService
) {
    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<Account>> {
        return ResponseEntity.ok(accountService.findAll())
    }
}