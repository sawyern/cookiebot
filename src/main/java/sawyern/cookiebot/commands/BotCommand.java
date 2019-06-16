package sawyern.cookiebot.commands;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import sawyern.cookiebot.exception.CookieException;

import java.util.List;

public interface BotCommand {
    void execute(MessageCreateEvent event, List<String> args) throws CookieException;
    void subscribeCommand(DiscordClient client);
}
