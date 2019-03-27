package sawyern.cookiebot;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.bot.BotCommand;
import sawyern.cookiebot.properties.DiscordClientProperties;

import java.util.List;

@Component
@Slf4j
public class CookieBotDiscordClient {
    private static DiscordClient discordClient;

    private DiscordClientProperties discordClientProperties;
    private List<BotCommand> botCommands;

    @EventListener(ApplicationReadyEvent.class)
    public void startDiscordClient() {
        setDiscordClient(new DiscordClientBuilder(discordClientProperties.getToken()).build());
        subscribeAllCommands(discordClient);
        discordClient.login().block();
    }

    public void subscribeAllCommands(DiscordClient client) {
        client.getEventDispatcher()
                .on(ReadyEvent.class)
                .subscribe(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()));
        botCommands.forEach(command -> command.subscribeCommand(client));
    }

    public static DiscordClient getDiscordClient() {
        return CookieBotDiscordClient.getDiscordClient();
    }

    private static void setDiscordClient(DiscordClient discordClient) {
        CookieBotDiscordClient.discordClient = discordClient;
    }

    @Autowired
    public void setDiscordClientProperties(DiscordClientProperties discordClientProperties) {
        this.discordClientProperties = discordClientProperties;
    }

    @Autowired
    public void setBotCommands(List<BotCommand> botCommands) {
        this.botCommands = botCommands;
    }
}
