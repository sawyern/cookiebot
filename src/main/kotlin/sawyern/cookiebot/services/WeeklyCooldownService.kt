package sawyern.cookiebot.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import sawyern.cookiebot.constants.Constants
import sawyern.cookiebot.models.WeeklyCooldown
import sawyern.cookiebot.repository.WeeklyCooldownRepository

@Service
class WeeklyCooldownService(
        private val weeklyCooldownRepository: WeeklyCooldownRepository,
        private val accountService: AccountService
) {
    private val logger: Logger = LoggerFactory.getLogger(WeeklyCooldownService::class.java)

    fun isCooldown(discordId: String): Boolean {
        return getCooldown(discordId).onCooldown
    }

    fun getCooldown(discordId: String): WeeklyCooldown {
        var cooldown = weeklyCooldownRepository.findByAccountDiscordId(discordId)
        if (cooldown == null)
            cooldown = createCooldown(discordId)
        return cooldown
    }

    fun triggerCooldown(discordId: String) {
        val cooldown = getCooldown(discordId)
        cooldown.onCooldown = true
        weeklyCooldownRepository.save(cooldown)
    }

    fun createCooldown(discordId: String): WeeklyCooldown {
        val account = accountService.getAccount(discordId)
        var cooldown = weeklyCooldownRepository.findByAccountDiscordId(discordId)
        if (cooldown == null) {
            cooldown = WeeklyCooldown(account, false)
            cooldown = weeklyCooldownRepository.save(cooldown)
            logger.info("New cooldown created.")
        }
        return cooldown
    }

    @Scheduled(cron = Constants.Time.RESET_TIME, zone = Constants.Time.TIME_ZONE_EST)
    fun resetCooldowns() {
        val cooldowns = weeklyCooldownRepository.findAll()
        cooldowns.forEach { cd -> cd.onCooldown = false }
        weeklyCooldownRepository.saveAll(cooldowns)
        logger.info("Cooldowns reset.")
    }
}