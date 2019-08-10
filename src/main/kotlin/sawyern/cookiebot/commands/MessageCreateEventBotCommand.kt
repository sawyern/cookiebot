package sawyern.cookiebot.commands

import discord4j.core.DiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.Assert
import sawyern.cookiebot.constants.CommandConstants
import sawyern.cookiebot.exception.AssertMessage
import sawyern.cookiebot.exception.CookieException
import sawyern.cookiebot.exception.DiscordException
import sawyern.cookiebot.exception.InvalidCommandArgumentLengthCookieException
import sawyern.cookiebot.services.BotUtilService
import java.util.*
import kotlin.collections.LinkedHashSet

abstract class MessageCreateEventBotCommand  @Autowired constructor(
        protected val botUtilService: BotUtilService
) : BotCommand {

    private val logger: Logger = LoggerFactory.getLogger(MessageCreateEventBotCommand::class.java)
    
    override fun subscribe(client: DiscordClient) {
        client.eventDispatcher.on(MessageCreateEvent::class.java).subscribe {
            event -> this.executeCommand(event)
        }
    }

    abstract fun getCommand(): String
    abstract fun getHelpText(): String
    open fun getAllowedNumArgs(): Set<Int> = LinkedHashSet()

    internal fun executeCommand(event: MessageCreateEvent) {
        try {
            // get text content from message, ignore non-text messages
            val content = event.message.content.orElseThrow { DiscordException("Error getting message content") }

            // only process messages that start with "!", optimization
            if (!content.startsWith(CommandConstants.COMMAND_START))
                throw DiscordException("Command does not start with \"${CommandConstants.COMMAND_START}\"")

            val command = parseCommand(content)
            val args = parseArgs(content)

            if (command == getCommand()) {
                logger.info("Received command: {}", getCommand())

                checkValidNumArgs(args)

                execute(event, args)
            }
        } catch (e: CookieException) {
            // known exception caught and handled. printed here
            logger.error(e.message)
            botUtilService.sendMessage(event, e.message ?: "Unknown error.")
        } catch (e: DiscordException) {
            // know exception logged, but not sent to client
            logger.debug(e.message)
        } catch (e: Exception) {
            // log unknown exceptions
            logger.error(e.message, e)
            botUtilService.sendMessage(event, "An unexpected error occurred. Contact an admin to check the logs for details.")
        }
    }

    internal fun checkValidNumArgs(args: List<String>) {
        // empty means any number of arguments is okay
        if (getAllowedNumArgs().isEmpty())
            return

        if (!getAllowedNumArgs().contains(args.size))
            throw InvalidCommandArgumentLengthCookieException(getAllowedNumArgs())
    }

    internal fun parseArgs(message: String): List<String> {
        Assert.isTrue(message.isNotEmpty(), AssertMessage.EMPTY_MESSAGE)
        val splitMessage = LinkedList(botUtilService.splitMessage(message))

        // ignore first elt as it is the command, only return list of args
        splitMessage.removeFirst()

        return splitMessage
    }

    internal fun parseCommand(message: String): String {
        Assert.isTrue(message.isNotEmpty(), AssertMessage.EMPTY_MESSAGE)
        val command = botUtilService.splitMessage(message)
                .stream()
                .findFirst()
                .orElse(CommandConstants.COMMAND_START + CommandConstants.CommandName.UNKNOWN)

        return if (!command.startsWith(CommandConstants.COMMAND_START))
            CommandConstants.CommandName.UNKNOWN
        else command.split(CommandConstants.COMMAND_START)[1]
    }
}