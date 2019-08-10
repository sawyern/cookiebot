package sawyern.cookiebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import sawyern.cookiebot.models.Prestige

interface PrestigeRepository: JpaRepository<Prestige, String> {
    fun findByAccountDiscordId(discordId: String): Prestige?
}