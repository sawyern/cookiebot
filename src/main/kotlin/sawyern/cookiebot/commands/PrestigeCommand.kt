package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.stereotype.Component
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.PrestigeService

@Component
class PrestigeCommand(
        botUtilService: BotUtilService,
        private val prestigeService: PrestigeService
): MessageCreateEventBotCommand(botUtilService) {

    override fun getCommand(): String {
        return "prestige"
    }

    override fun getHelpText(): String {
        return "Returns prestige for account. Prestige is based on the cookies you won in a previous season. This value never gets reset."
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        val member = botUtilService.getMember(event)
        val prestige = prestigeService.getPrestige(member.id.asString())
        botUtilService.sendMessage("${member.username} has ${prestige.value} prestige")
    }
}