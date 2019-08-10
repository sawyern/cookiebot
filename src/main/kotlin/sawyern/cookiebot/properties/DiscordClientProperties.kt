package sawyern.cookiebot.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "discord")
class DiscordClientProperties {
    lateinit var token: String
    lateinit var botChannelId: String
}
