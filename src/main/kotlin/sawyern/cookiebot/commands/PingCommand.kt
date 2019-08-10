package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sawyern.cookiebot.constants.CommandConstants
import sawyern.cookiebot.properties.AppProperties
import sawyern.cookiebot.services.BotUtilService

@Component
class PingCommand @Autowired constructor(
        private val appProperties: AppProperties,
        botUtilService: BotUtilService
) : MessageCreateEventBotCommand(botUtilService)
{
    override fun getCommand(): String = CommandConstants.CommandName.PING
    override fun getHelpText(): String = "Checks if the server is online and returns the version"

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        botUtilService.sendMessage(event, "pong! v ${appProperties.version}")
    }
}