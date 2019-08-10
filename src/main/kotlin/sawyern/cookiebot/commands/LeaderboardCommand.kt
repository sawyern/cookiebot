package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sawyern.cookiebot.services.AccountService
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.CookieService
import java.util.HashMap

@Component
class LeaderboardCommand @Autowired constructor(
        botUtilService: BotUtilService,
        private val cookieService: CookieService,
        private val accountService: AccountService
): MessageCreateEventBotCommand(botUtilService) {
    override fun getCommand(): String {
        return "leaderboard"
    }

    override fun getHelpText(): String {
        return "get seasonal ranking of cookies"
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        val leaderboard = StringBuilder("```")
        var leaderboardMap = HashMap<String, Int>()

        val accounts = accountService.findAll()

        for (account in accounts) {
            val cookies = cookieService.getCookiesForAccount(account.discordId)
            leaderboardMap[account.username] = cookies
        }

        var sortedLeaderboardMap = leaderboardMap
                .toList()
                .sortedBy { (key, value) -> value }
                .asReversed()
                .toMap()

        for ((key, value) in sortedLeaderboardMap) {
            leaderboard.append(key)
                    .append(": ")
                    .append(value)
                    .append("\n")
        }

        leaderboard.append("```")
        botUtilService.sendMessage(event, leaderboard.toString())
    }
}