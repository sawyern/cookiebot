package sawyern.cookiebot.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import sawyern.cookiebot.models.Prestige
import sawyern.cookiebot.repository.PrestigeRepository

@Service
class PrestigeService @Autowired constructor(
        private val prestigeRepository: PrestigeRepository,
        private val accountService: AccountService
) {

    private val logger: Logger = LoggerFactory.getLogger(PrestigeService::class.java)
    
    fun getPrestige(discordId: String): Prestige {
        var prestige = prestigeRepository.findByAccountDiscordId(discordId)
        if (prestige == null)
            prestige = createPrestige(discordId)
        return prestige
    }

    fun createPrestige(discordId: String): Prestige {
        val existingPrestige = prestigeRepository.findByAccountDiscordId(discordId)
        if (existingPrestige != null)
            return existingPrestige

        val account = accountService.getAccount(discordId)
        val prestige = Prestige(account, 0)

        logger.info("Prestige created for account $discordId")

        return prestigeRepository.save(prestige)
    }

    fun addPrestige(discordId: String, amount: Int) : Prestige {
        val prestige = getPrestige(discordId)
        prestige.value += amount
        logger.info("$amount prestige added to account $discordId")
        return prestigeRepository.save(prestige)
    }
}