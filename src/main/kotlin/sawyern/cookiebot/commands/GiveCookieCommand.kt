package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sawyern.cookiebot.exception.CookieException
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.CookieService
import java.text.MessageFormat

@Component
class GiveCookieCommand @Autowired constructor(
        botUtilService: BotUtilService,
        private val cookieService: CookieService
): MessageCreateEventBotCommand(botUtilService) {
    override fun getAllowedNumArgs(): Set<Int> {
        return setOf(1, 2)
    }

    override fun getCommand(): String {
        return "givecookie"
    }

    override fun getHelpText(): String {
        return "give cookie to {receiver}"
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        val senderUser = botUtilService.getMember(event)
        val receiverUser: String = args[0]
        val receiverId: String = botUtilService.getIdFromUser(event, args[0])
        var numCookies = 1

        if (receiverUser.equals(botUtilService.getMember(event).username, ignoreCase = true))
            throw CookieException("Can't give cookies to yourself.")

        if (args.size == 2)
            numCookies = botUtilService.parseIntArgument(args[1], true)

        cookieService.giveCookieTo(senderUser.id.asString(), receiverId, numCookies)

        val senderTotal = cookieService.getCookiesForAccount(senderUser.id.asString())
        val receiverTotal = cookieService.getCookiesForAccount(receiverId)

        botUtilService.sendMessage(event, MessageFormat.format(
                "Successfully transferred cookies. {0}: {1}, {2}: {3}",
                senderUser.username,
                senderTotal.toString(),
                receiverUser,
                receiverTotal.toString())
        )
    }
}