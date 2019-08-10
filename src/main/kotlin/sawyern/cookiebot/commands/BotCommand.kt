package sawyern.cookiebot.commands

import discord4j.core.DiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent

interface BotCommand {
    fun execute(event: MessageCreateEvent, args: List<String>)
    fun subscribe(client: DiscordClient)
}
