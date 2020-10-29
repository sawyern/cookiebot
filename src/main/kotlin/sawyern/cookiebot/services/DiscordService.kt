package sawyern.cookiebot.services

import discord4j.common.util.Snowflake
import discord4j.core.DiscordClient
import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.channel.MessageChannel
import discord4j.core.event.domain.lifecycle.ReadyEvent
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import sawyern.cookiebot.commands.BotCommand
import sawyern.cookiebot.properties.DiscordClientProperties

@Service
class DiscordService @Autowired
constructor(
        private val discordClientProperties: DiscordClientProperties,
        private val botCommands: List<BotCommand>
) {
    private val discordClient: DiscordClient by lazy {
        DiscordClient.create(discordClientProperties.token)
    }

    private val logger = KotlinLogging.logger {}

    internal fun subscribeReady(gateway : GatewayDiscordClient) {
        gateway.on(ReadyEvent::class.java).subscribe { logger.info("Logged in as {}", it.self.username) }
    }

    internal fun subscribeAllCommands(gateway : GatewayDiscordClient) {
        botCommands.forEach { command -> command.subscribe(gateway) }
    }

    internal fun login() : GatewayDiscordClient? { return discordClient.login().block() }

    internal fun getBotChannel(): MessageChannel {
        return discordClient
                .getChannelById(Snowflake.of(discordClientProperties.botChannelId))
                as MessageChannel
    }
}




