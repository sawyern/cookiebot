package sawyern.cookiebot.controllers

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.SeasonService

@Controller
@RequestMapping("/api/cookiebot/v1/season")
class SeasonController(
        private val seasonService: SeasonService,
        private val botUtilService: BotUtilService
) {
    @PostMapping
    fun startNewSeason(@RequestParam seasonName: String): ResponseEntity<String> {
        seasonService.startNewSeason(seasonName)
        botUtilService.sendMessage("New season starting! Season: $seasonName. All cookies from previous season have been converted to prestige.")
        return ResponseEntity.ok("New season created: $seasonName")
    }
}