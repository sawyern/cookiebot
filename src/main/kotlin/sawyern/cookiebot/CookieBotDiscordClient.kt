package sawyern.cookiebot

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import sawyern.cookiebot.services.DiscordService
import sawyern.cookiebot.services.SeasonService

@Component
class CookieBotDiscordClient @Autowired constructor(
        private val discordService: DiscordService,
        private val seasonService: SeasonService,
        private val environment: Environment
) {
    private val logger: Logger = LoggerFactory.getLogger(CookieBotDiscordClient::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun startDiscordClient() {
        addTestData()
        val gateway = discordService.login()
        if (gateway != null) {
            discordService.subscribeReady(gateway)
            discordService.subscribeAllCommands(gateway)
            gateway.onDisconnect().block()
        }
    }

    fun addTestData() {
        if (environment.activeProfiles.contains("local")) {
            logger.info("inputting test data")
            seasonService.startNewSeason("Test Season")
        }
    }
}
