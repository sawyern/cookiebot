package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.stereotype.Component
import sawyern.cookiebot.services.AccountService
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.PrestigeService
import java.util.HashMap

@Component
class LeaderboardPCommand(
        botUtilService: BotUtilService,
        private val accountService: AccountService,
        private val prestigeService: PrestigeService
): MessageCreateEventBotCommand(botUtilService) {
    override fun getCommand(): String {
        return "leaderboardp"
    }

    override fun getHelpText(): String {
        return "get leaderboard of prestige for server"
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        val leaderboard = StringBuilder("```")
        var leaderboardMap = HashMap<String, Int>()

        val accounts = accountService.findAll()

        for (account in accounts) {
            val prestige = prestigeService.getPrestige(account.discordId)
            leaderboardMap[account.username] = prestige.value
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