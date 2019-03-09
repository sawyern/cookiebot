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
    private DiscordClient discordClient;

    private DiscordClientProperties discordClientProperties;
    private List<BotCommand> botCommands;

    private static Logger LOGGER = LoggerFactory.getLogger(CookieBotDiscordClient.class);

    @EventListener(ApplicationReadyEvent.class)
    public void startDiscordClient() {
        this.discordClient = new DiscordClientBuilder(discordClientProperties.getToken()).build();
        subscribeAllCommands(discordClient);
        discordClient.login().block();
    }

    public void subscribeAllCommands(DiscordClient client) {
        client.getEventDispatcher()
                .on(ReadyEvent.class)
                .subscribe(ready -> LOGGER.info("Logged in as " + ready.getSelf().getUsername()));
        botCommands.forEach(command -> command.subscribeCommand(client));
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
