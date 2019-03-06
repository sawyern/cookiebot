package sawyern.cookiebot;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.bot.BotCommand;
import sawyern.cookiebot.properties.DiscordClientProperties;

import java.util.List;

@Component
public class CookieBotDiscordClient {
    private DiscordClientProperties discordClientProperties;
    private DiscordClient discordClient;
    private List<BotCommand> botCommands;

    private static Logger LOGGER = LoggerFactory.getLogger(CookieBotDiscordClient.class);

    @Autowired
    public CookieBotDiscordClient(
            DiscordClientProperties discordClientProperties,
            List<BotCommand> botCommands
    ) {
        this.discordClientProperties = discordClientProperties;
        this.botCommands = botCommands;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startDiscordClient() {
        this.discordClient = this.createClient();
        setupClient(discordClient);
        discordClient.login().block();
    }

    public DiscordClient createClient() {
        String token = discordClientProperties.getToken();
        return new DiscordClientBuilder(token).build();
    }

    public void setupClient(DiscordClient client) {
        client.getEventDispatcher().on(ReadyEvent .class)
                .subscribe(ready -> LOGGER.info("Logged in as " + ready.getSelf().getUsername()));
        botCommands.forEach(command -> {
            command.subscribeCommand(client);
        });
    }

    public List<BotCommand> getBotCommands() {
        return botCommands;
    }

    public void setBotCommands(List<BotCommand> botCommands) {
        this.botCommands = botCommands;
    }
}
