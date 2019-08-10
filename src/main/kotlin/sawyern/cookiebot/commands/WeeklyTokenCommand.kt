package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sawyern.cookiebot.constants.Constants
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.LootboxTokenService
import sawyern.cookiebot.services.WeeklyCooldownService

@Component
class WeeklyTokenCommand @Autowired constructor(
        botUtilService: BotUtilService,
        private val weeklyCooldownService: WeeklyCooldownService,
        private val lootboxTokenService: LootboxTokenService
): MessageCreateEventBotCommand(botUtilService) {
    override fun getCommand(): String {
        return "weeklytokens"
    }

    override fun getHelpText(): String {
        return "get your weekly lootboxes once a week"
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        val id = botUtilService.getMember(event).id.asString()
        if (!weeklyCooldownService.isCooldown(id)) {
            weeklyCooldownService.triggerCooldown(id)
            lootboxTokenService.generateLootbox(id, Constants.Cookie.WEEKLY_LOOTBOXES)
            botUtilService.sendMessage("${Constants.Cookie.WEEKLY_LOOTBOXES} tokens have been awarded. Good luck!")
        } else {
            botUtilService.sendMessage("Weekly cooldown is not reset. Reset happens every week at MON 8pm EST.")
        }
    }
}