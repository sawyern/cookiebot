package sawyern.cookiebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import sawyern.cookiebot.models.Cookie
import sawyern.cookiebot.models.Season

interface CookieRepository: JpaRepository<Cookie, String> {
    fun findByAccountDiscordId(discordId: String): Collection<Cookie>
    fun findByAccountDiscordIdAndSeason(discordId: String, season: Season): Collection<Cookie>
}