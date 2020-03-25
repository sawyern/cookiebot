package sawyern.cookiebot.controllers;

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sawyern.cookiebot.services.LootboxTokenService

@Controller
@RequestMapping("/api/cookiebot/v1/tokens")
class TokenController (private val tokenService: LootboxTokenService) {
    @PostMapping
    fun addTokens(@RequestParam discordId: String, numLootboxes: Int): ResponseEntity<String> {
        tokenService.generateLootbox(discordId, numLootboxes)
        return ResponseEntity.ok("Success")
    }
}