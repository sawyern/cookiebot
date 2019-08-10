package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sawyern.cookiebot.constants.CommandConstants
import sawyern.cookiebot.exception.CookieException
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.CookieService
import java.text.MessageFormat

@Component
class CookiesCommand @Autowired constructor(
        botUtilService: BotUtilService,
        private val cookieService: CookieService
) : MessageCreateEventBotCommand(botUtilService) {
    override fun getCommand(): String {
        return CommandConstants.CommandName.COOKIES
    }

    override fun getHelpText(): String {
        return "get cookie count"
    }

    override fun getAllowedNumArgs(): Set<Int> {
        return setOf(0, 1)
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        var discordId = ""
        val numCookies: Int
        var username = ""

        if (args.isEmpty()) {
            val member = event.member.orElseThrow { CookieException("Error getting member.") }
            discordId = member.id.asString()
            username = member.username
        } else if (args.size == 1) {
            discordId = botUtilService.getIdFromUser(event, args[0])
            username = args[0]
        }

        numCookies = cookieService.getCookiesForAccount(discordId)
        botUtilService.sendMessage(event, MessageFormat.format("{0} has {1} cookies.", username, numCookies))
    }
}