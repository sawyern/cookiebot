package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.stereotype.Component
import sawyern.cookiebot.exception.CookieException
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.CookieService
import sawyern.cookiebot.services.LootboxTokenService
import java.lang.StringBuilder

@Component
class LootboxCommand(
        botUtilService: BotUtilService,
        private val lootboxTokenService: LootboxTokenService,
        private val cookieService: CookieService
): MessageCreateEventBotCommand(botUtilService) {
    override fun getAllowedNumArgs(): Set<Int> {
        return setOf(0, 1)
    }

    override fun getCommand(): String {
        return "lootbox"
    }

    override fun getHelpText(): String {
        return "open lootbox, add a number to open many"
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        var numCookiesWon = 0
        var titanForging = true
        var odds: Double = 50.0
        val id = botUtilService.getMember(event).id.asString()
        var numBoxes = 1
        val builder = StringBuilder("```")
        var currentLootboxes = lootboxTokenService.getLootboxesForAccount(id)

        if (args.size == 1) {
            numBoxes = botUtilService.parseIntArgument(args[0], true)
        }


        if (lootboxTokenService.getLootboxesForAccount(id) < numBoxes)
            throw CookieException("Not enough tokens.")


        for (i in 1..numBoxes) {
            numCookiesWon = 0
            if (roll() <= odds)
                titanForging = false

            while (titanForging) {
                titanForging = roll() <= 50
                numCookiesWon++
            }

            lootboxTokenService.deleteLootbox(id)
            botUtilService.sendMessage(event, "Spending token. Remaining tokens: ${lootboxTokenService.getLootboxesForAccount(id)}")
            cookieService.generateCookie(id, numCookiesWon)
            builder.append("You open the lootbox!\nContains...$numCookiesWon cookies!\n")

        }
        val newTotal = cookieService.getCookiesForAccount(id)
        builder.append("${botUtilService.getMember(event).username} cookies: $newTotal tokens: ${currentLootboxes - numBoxes}")
        builder.append("```")
        botUtilService.sendMessage(builder.toString())
    }

    private fun roll(): Double {
        val max = 100
        val min = 1
        return Math.random() * (max - min + 1) + min
    }
}