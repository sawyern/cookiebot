package sawyern.cookiebot.services;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sawyern.cookiebot.commands.BotCommand;
import sawyern.cookiebot.exception.AssertMessage;
import sawyern.cookiebot.properties.DiscordClientProperties;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscordService {
    private final DiscordClientProperties discordClientProperties;
    private final List<BotCommand> botCommands;

    private DiscordClient discordClient;

    /**
     * Create discord client given token from properties file
     */
    public void setDiscordClient() {
        Assert.notNull(discordClientProperties, AssertMessage.ERROR_LOADING_PROPERTY);
        Assert.notNull(discordClientProperties.getToken(), AssertMessage.DISCORD_TOKEN_NOT_SET);
        discordClient = new DiscordClientBuilder(discordClientProperties.getToken()).build();
    }

    /**
     * Subscribe commands to discord events
     */
    public void subscribeAllCommands() {
        Assert.notNull(discordClient, AssertMessage.DISCORD_CLIENT_NOT_SET);
        botCommands.forEach(command -> command.subscribeCommand(discordClient));
    }

    /**
     * log ready event
     */
    public void subscribeReady() {
        Assert.notNull(discordClient, AssertMessage.DISCORD_CLIENT_NOT_SET);
        discordClient.getEventDispatcher()
                .on(ReadyEvent.class)
                .subscribe(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()));
    }

    /**
     * discord client will log into server
     */
    public void login() {
        Assert.notNull(discordClient, AssertMessage.DISCORD_CLIENT_NOT_SET);
        discordClient.login().block();
    }
}
