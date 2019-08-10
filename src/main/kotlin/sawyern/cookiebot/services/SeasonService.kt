package sawyern.cookiebot.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import sawyern.cookiebot.constants.Constants
import sawyern.cookiebot.exception.CookieException
import sawyern.cookiebot.models.Season
import sawyern.cookiebot.repository.SeasonRepository
import java.util.HashMap
import javax.transaction.Transactional
import kotlin.math.roundToInt

@Service
class SeasonService @Autowired constructor(
        private val seasonRepository: SeasonRepository,
        private val prestigeService: PrestigeService,
        @Lazy
        private val accountService: AccountService,
        @Lazy
        private val cookieService: CookieService
) {
    
    private val logger: Logger = LoggerFactory.getLogger(SeasonService::class.java)
    
    fun getCurrentSeason(): Season {
        return seasonRepository.findByStatus(Constants.SEASON_ACTIVE) ?: throw CookieException("No Season found")
    }

    @Transactional
    fun startNewSeason(seasonName: String) {
        val currentSeason = seasonRepository.findByStatus(Constants.SEASON_ACTIVE)

        // create new season
        val newSeason = Season(seasonName, Constants.SEASON_ACTIVE)
        seasonRepository.save(newSeason)

        if (currentSeason != null) {

            currentSeason.status = Constants.SEASON_INACTIVE
            seasonRepository.save(currentSeason)

            addPrestige(currentSeason, newSeason)

            logger.info("Ending season ${currentSeason.name}")
        }

        logger.info("Starting season ${newSeason.name}")
    }

    fun addPrestige(oldSeason: Season, newSeason: Season) {
        val accounts = accountService.findAll()
        val accountMap = HashMap<String, Int>()

        logger.info("Calculating prestige earned from previous season...")

        for (account in accounts) {
            val cookies = cookieService.getCookiesForAccount(account.discordId, oldSeason)
            cookieService.generateCookie(account.discordId, Constants.Cookie.STARTING_COOKIES, newSeason)
            accountMap[account.discordId] = cookies
        }

        val sortedMap = accountMap
                .toList()
                .sortedBy { (key, value) -> value }
                .asReversed()
                .toMap()

        var place = 0
        for ((discordId, cookies) in sortedMap) {
            place++

            var prestigeMultiplier = 1.0

            when (place) {
                1 -> prestigeMultiplier = Constants.PrestigeMultiplier.FIRST_PLACE
                2 -> prestigeMultiplier = Constants.PrestigeMultiplier.SECOND_PLACE
                3 -> prestigeMultiplier = Constants.PrestigeMultiplier.THIRD_PLACE
            }

            prestigeService.addPrestige(discordId, (cookies * prestigeMultiplier).roundToInt())
            logger.info("Successfully calculated prestige for previous season.")
        }
    }
}