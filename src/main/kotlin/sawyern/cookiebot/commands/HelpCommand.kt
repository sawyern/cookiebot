package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sawyern.cookiebot.constants.CommandConstants
import sawyern.cookiebot.services.BotUtilService
import java.util.ArrayList

@Component
class HelpCommand @Autowired constructor(
        botUtilService: BotUtilService,
        private val botCommands: List<MessageCreateEventBotCommand>
): MessageCreateEventBotCommand(botUtilService) {
    override fun getCommand(): String {
        return CommandConstants.CommandName.HELP
    }

    override fun getHelpText(): String {
        return "shows all command help information"
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        val helpStrings = ArrayList<String>()
        botCommands.forEach { command ->
            helpStrings.add(
                    CommandConstants.COMMAND_START +
                            command.getCommand() +
                            " -- " +
                            command.getHelpText()
            )
        }

        val helpString = StringBuilder("```")
        for (str in helpStrings) {
            helpString.append(str)
            helpString.append("\n")
        }
        helpString.append("```")
        botUtilService.sendMessage(event, helpString.toString())
    }
}