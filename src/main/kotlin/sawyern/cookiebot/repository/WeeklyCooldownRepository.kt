package sawyern.cookiebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sawyern.cookiebot.models.WeeklyCooldown

@Repository
interface WeeklyCooldownRepository: JpaRepository<WeeklyCooldown, String> {
    fun findByAccountDiscordId(discordId: String): WeeklyCooldown?
}