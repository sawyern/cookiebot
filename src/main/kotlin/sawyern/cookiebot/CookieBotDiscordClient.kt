package sawyern.cookiebot

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import sawyern.cookiebot.services.DiscordService
import sawyern.cookiebot.services.SeasonService

@Component
class CookieBotDiscordClient @Autowired constructor(
        val discordService: DiscordService,
        val seasonService: SeasonService
) {
    private val logger: Logger = LoggerFactory.getLogger(CookieBotDiscordClient::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun startDiscordClient() {
        addTestData()
        discordService.subscribeReady()
        discordService.subscribeAllCommands()
        discordService.login()
    }

    fun addTestData() {
        logger.info("inputting test data")
        seasonService.startNewSeason("Test Season")
    }
}
