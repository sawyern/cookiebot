package sawyern.cookiebot.bot;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import sawyern.cookiebot.bot.exception.CookieException;

public interface BotCommand {
    void execute(MessageCreateEvent event) throws CookieException;
    void subscribeCommand(DiscordClient client);
}
