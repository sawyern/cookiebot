package sawyern.cookiebot.commands

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sawyern.cookiebot.services.BotUtilService
import sawyern.cookiebot.services.LootboxTokenService

@Component
class TokensCommand @Autowired constructor(
        botUtilService: BotUtilService,
        private val lootboxTokenService: LootboxTokenService
): MessageCreateEventBotCommand(botUtilService){
    override fun getAllowedNumArgs(): Set<Int> {
        return setOf(0, 1)
    }

    override fun getCommand(): String {
        return "tokens"
    }

    override fun getHelpText(): String {
        return "get number of tokens"
    }

    override fun execute(event: MessageCreateEvent, args: List<String>) {
        var id = botUtilService.getMember(event).id.asString()
        var username = botUtilService.getMember(event).username

        if (args.size == 1) {
            id = botUtilService.getIdFromUser(event, args[0])
            username = args[0]
        }

        val numLootboxes = lootboxTokenService.getLootboxesForAccount(id)

        botUtilService.sendMessage("$username has $numLootboxes tokens.")
    }
}