package sawyern.cookiebot.events

import org.springframework.context.ApplicationEvent


class DiscordLoginEvent(
        source: Any
): ApplicationEvent(source)