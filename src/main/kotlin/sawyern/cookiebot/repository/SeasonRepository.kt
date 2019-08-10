package sawyern.cookiebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import sawyern.cookiebot.models.Season
import java.util.*

interface SeasonRepository: JpaRepository<Season, String> {
    fun findByStatus(status: String): Season?
}