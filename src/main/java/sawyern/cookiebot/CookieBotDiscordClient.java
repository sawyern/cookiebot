package sawyern.cookiebot;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.commands.BotCommand;
import sawyern.cookiebot.properties.DiscordClientProperties;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CookieBotDiscordClient {
    private static DiscordClient discordClient;

    private final DiscordClientProperties discordClientProperties;
    private final List<BotCommand> botCommands;

    @EventListener(ApplicationReadyEvent.class)
    public void startDiscordClient() {
        // create new discord client given bot token
        setDiscordClient(new DiscordClientBuilder(discordClientProperties.getToken()).build());

        // print message on ready event
        subscribeReady(discordClient);

        // subscribe all commands to listen for
        subscribeAllCommands(discordClient);

        discordClient.login().block();
    }

    /**
     * Subscribe events to listen for
     * @param client
     */
    private void subscribeAllCommands(@NonNull DiscordClient client) {
        botCommands.forEach(command -> command.subscribeCommand(client));
    }

    private void subscribeReady(@NonNull DiscordClient client) {
        client.getEventDispatcher()
                .on(ReadyEvent.class)
                .subscribe(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()));
    }

    public static DiscordClient getDiscordClient() {
        return discordClient;
    }

    private static void setDiscordClient(DiscordClient discordClient) {
        CookieBotDiscordClient.discordClient = discordClient;
    }
}
