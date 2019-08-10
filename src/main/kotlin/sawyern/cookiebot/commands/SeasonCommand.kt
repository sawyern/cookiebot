package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.SeasonService

@Component
class SeasonCommand @Autowired constructor(
        botUtilService: BotUtilService,
        private val seasonService: SeasonService
): MessageCreateEventBotCommand(botUtilService) {
    override fun getCommand(): String {
        return "season"
    }

    override fun getHelpText(): String {
        return "get current season"
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        botUtilService.sendMessage("Current season is: ${seasonService.getCurrentSeason().name}")
    }
}