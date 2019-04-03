package sawyern.cookiebot.services;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sawyern.cookiebot.commands.BotCommand;
import sawyern.cookiebot.exception.AssertMessage;
import sawyern.cookiebot.properties.DiscordClientProperties;

import java.util.List;

@Service
@Slf4j
@NoArgsConstructor
public class DiscordService {

    @Setter
    private DiscordClientProperties discordClientProperties;
    @Setter
    private List<BotCommand> botCommands;

    @Getter(AccessLevel.PROTECTED)
    private DiscordClient discordClient;

    /**
     * Create discord client given token from properties file
     */
    public void setDiscordClient() {
        if (getDiscordClient() != null)
            return;

        Assert.state(discordClientProperties != null, AssertMessage.ERROR_LOADING_PROPERTY);
        Assert.state(discordClientProperties.getToken() != null, AssertMessage.DISCORD_TOKEN_NOT_SET);
        discordClient = new DiscordClientBuilder(discordClientProperties.getToken()).build();
    }

    /**
     * Subscribe commands to discord events
     */
    public void subscribeAllCommands() {
        Assert.state(getDiscordClient() != null, AssertMessage.DISCORD_CLIENT_NOT_SET);
        Assert.state(botCommands != null, AssertMessage.NULL_BOT_COMMANDS);
        botCommands.forEach(command -> command.subscribeCommand(getDiscordClient()));
    }

    /**
     * log ready event
     */
    public void subscribeReady() {
        Assert.state(getDiscordClient() != null, AssertMessage.DISCORD_CLIENT_NOT_SET);
        getDiscordClient().getEventDispatcher()
                .on(ReadyEvent.class)
                .subscribe(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()));
    }

    /**
     * discord client will log into server
     */
    public void login() {
        Assert.state(getDiscordClient() != null, AssertMessage.DISCORD_CLIENT_NOT_SET);
        getDiscordClient().login().block();
    }
}
