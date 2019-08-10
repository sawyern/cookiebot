package sawyern.cookiebot.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import sawyern.cookiebot.exception.CookieException
import sawyern.cookiebot.models.Cookie
import sawyern.cookiebot.models.Season
import sawyern.cookiebot.repository.CookieRepository

@Service
class CookieService @Autowired constructor(
        private val cookieRepository: CookieRepository,
        private val seasonService: SeasonService,
        @Lazy
        private val accountService: AccountService

) {
    private val logger: Logger = LoggerFactory.getLogger(CookieService::class.java)
    
    fun giveCookieTo(senderId: String, receiverId: String, numCookies: Int) {
        if (numCookies <= 0)
            throw CookieException("Invalid number of cookies: $numCookies")

        val senderCookies = cookieRepository.findByAccountDiscordIdAndSeason(senderId, seasonService.getCurrentSeason())

        if (senderCookies.size < numCookies)
            throw CookieException("Not enough cookies to give")

        var cookiesGiven = 0
        val receiverAccount = accountService.getAccount(receiverId)
        for (cookie in senderCookies) {
            cookie.account = receiverAccount
            cookieRepository.save(cookie)
            cookiesGiven++

            if (cookiesGiven == numCookies)
                break
        }

        if (logger.isInfoEnabled) {
            val senderAccount = accountService.getAccount(senderId)
            logger.info("Transferred $numCookies cookies from ${senderAccount.username} to ${receiverAccount.username}")
        }
    }

    fun getCookiesForAccount(discordId: String, season: Season = seasonService.getCurrentSeason()): Int {
        return cookieRepository.findByAccountDiscordIdAndSeason(discordId, season).size
    }

    fun generateCookie(discordId: String, numCookies: Int = 1, season: Season = seasonService.getCurrentSeason()) {
        val account = accountService.getAccount(discordId)

        for(i in 1..numCookies) {
            val cookie = Cookie(account, season)
            cookieRepository.save(cookie)
        }

        logger.info("Generated $numCookies cookies for ${account.username}")
    }
}