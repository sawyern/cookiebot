package sawyern.cookiebot.services

import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.Message
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.discordjson.json.MessageData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import sawyern.cookiebot.constants.CommandConstants
import sawyern.cookiebot.exception.AssertMessage
import sawyern.cookiebot.exception.CookieException
import sawyern.cookiebot.exception.InvalidNumberParamCookieException
import java.util.ArrayList
import java.util.regex.Pattern

@Service
class BotUtilService @Autowired constructor(
        @Lazy private val discordService: DiscordService,
        @Lazy private val accountService: AccountService
) {
    private val logger: Logger = LoggerFactory.getLogger(BotUtilService::class.java)

    fun sendMessage(message: String): MessageData? {
        logger.info("Sending message: \"$message\"")
        return discordService.getBotChannel().createMessage(message).block()
    }

    fun sendMessage(event: MessageCreateEvent, message: String): Message? {
        val messageObj: Message?
        try {
            messageObj = event.message.channel.block()!!.createMessage(message).block()
            logger.info("Sending message: $message")
        } catch (e: Exception) {
            logger.error(e.message, e)
            throw CookieException("Error sending message.")
        }
        return messageObj
    }

    fun getIdFromUser(event: MessageCreateEvent, username: String): String {
        return accountService.getAccountByName(username).discordId
    }

    fun getMember(event: MessageCreateEvent): Member {
        return event.member.orElseThrow { CookieException("Error getting member.") }
    }

    fun getSelfId(event: MessageCreateEvent): String {
        return event.client.selfId.asString()
    }

    fun parseIntArgument(arg: String, isPositive: Boolean): Int {
        val parsedArg: Int
        try {
            parsedArg = Integer.parseInt(arg)
            if (isPositive && parsedArg <= 0)
                throw NumberFormatException()
        } catch (e: NumberFormatException) {
            logger.error(e.message, e)
            throw InvalidNumberParamCookieException(arg)
        }
        return parsedArg
    }

    fun splitMessage(message: String): List<String> {
        Assert.isTrue(message.isNotEmpty(), AssertMessage.EMPTY_MESSAGE)

        val msgArgs = ArrayList<String>()

        // split string by space, group ""
        val m = Pattern.compile(CommandConstants.QUOTE_REGEX).matcher(message)
        while (m.find())
            msgArgs.add(m.group(1).replace(CommandConstants.QUOTE, CommandConstants.EMPTY_STRING))
        return msgArgs
    }
}