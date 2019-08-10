package sawyern.cookiebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sawyern.cookiebot.models.LootboxToken
import sawyern.cookiebot.models.Season

@Repository
interface LootboxTokenRepository: JpaRepository<LootboxToken, String> {
    fun findByAccountDiscordId(discordId: String): Collection<LootboxToken>
    fun findByAccountDiscordIdAndSeason(discordId: String, season: Season): Collection<LootboxToken>
}