package sawyern.cookiebot.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sawyern.cookiebot.exception.CookieException
import sawyern.cookiebot.models.LootboxToken
import sawyern.cookiebot.repository.LootboxTokenRepository

@Service
class LootboxTokenService(
        private val lootboxTokenRepository: LootboxTokenRepository,
        private val accountService: AccountService,
        private val seasonService: SeasonService
) {

    val logger: Logger = LoggerFactory.getLogger(LootboxToken::class.java)

    fun generateLootbox(discordId: String, numLootboxes: Int) {
        val account = accountService.getAccount(discordId)

        for (i in 1..numLootboxes) {
            val token = LootboxToken(account, seasonService.getCurrentSeason())
            lootboxTokenRepository.save(token)
        }
        logger.info("Created $numLootboxes tokens for ${account.username}")
    }

    fun getLootboxesForAccount(discordId: String): Int {
        return lootboxTokenRepository.findByAccountDiscordIdAndSeason(discordId, seasonService.getCurrentSeason()).size
    }

    fun deleteLootbox(discordId: String, numTokens: Int = 1) {
        val tokens = lootboxTokenRepository.findByAccountDiscordId(discordId)

        if (tokens.size < numTokens)
            throw CookieException("Not enough tokens.")

        var i = 0
        for (token: LootboxToken in tokens) {
            if (i >= numTokens)
                break
            lootboxTokenRepository.delete(token)
            i++
        }

        if (logger.isInfoEnabled) {
            val account = accountService.getAccount(discordId)
            logger.info("Removing $numTokens from ${account.username}")
        }
    }
}