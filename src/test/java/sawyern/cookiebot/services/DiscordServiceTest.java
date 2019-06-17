package sawyern.cookiebot.services;

import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sawyern.cookiebot.TestConstants;
import sawyern.cookiebot.commands.BotCommand;
import sawyern.cookiebot.properties.DiscordClientProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class DiscordServiceTest {

    @InjectMocks
    @Spy
    private DiscordService discordService;

    @Mock
    private DiscordClientProperties discordClientProperties;

    @Mock
    private List<BotCommand> botCommands = new ArrayList<>();

    @Mock
    private BotCommand botCommand;
    @Mock
    private DiscordClient discordClient;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private Flux<ReadyEvent> readyFlux;
    @Mock
    private Mono<Void> mono;
    @Mock
    private ReadyEvent readyEvent;
    @Mock
    private User user;

    @Test
    public void setDiscordClientWhenNonNullClient() {
        mockNonNullClient();
        discordService.setDiscordClient();
    }

    @Test(expected = IllegalStateException.class)
    public void setDiscordClientWhenNullPropertiesThenThrowException() {
        mockNullClient();
        discordService = new DiscordService(null, botCommands);
        discordService.setDiscordClient();
    }

    @Test(expected = IllegalStateException.class)
    public void setDiscordClientWhenNullTokenThenThrowException() {
        mockNullClient();
        Mockito.when(discordClientProperties.getToken()).thenReturn(null);
        discordService.setDiscordClient();
    }

    @Test
    public void setDiscordClientWhenNullClient() {
        mockNullClient();
        Mockito.when(discordClientProperties.getToken()).thenReturn(TestConstants.DISCORD_TOKEN);
        discordService.setDiscordClient();
    }

    @Test(expected = IllegalStateException.class)
    public void subscribeAllCommandsWhenNullClientThenThrowException() {
        mockNullClient();
        discordService.subscribeAllCommands();
    }

    @Test(expected = IllegalStateException.class)
    public void subscribeAllCommandsWhenNullBotCommandsThenThrowException() {
        mockNonNullClient();
        discordService = new DiscordService(discordClientProperties, null);
        discordService.subscribeAllCommands();
    }

    @Test(expected = IllegalStateException.class)
    public void subscribeReadyWhenNullClientThenThrowException() {
        mockNullClient();
        discordService.subscribeReady();
    }

    @Test
    public void subscribeReady() {
        readyFlux = Flux.just(readyEvent);
        mockNonNullClient();
        Mockito.when(discordClient.getEventDispatcher()).thenReturn(eventDispatcher);
        Mockito.when(eventDispatcher.on(Mockito.eq(ReadyEvent.class))).thenReturn(readyFlux);
        Mockito.when(readyEvent.getSelf()).thenReturn(user);
        Mockito.when(user.getUsername()).thenReturn("testuser");
        discordService.subscribeReady();
    }

    @Test(expected = IllegalStateException.class)
    public void loginWhenNullClientThenThrowException() {
        mockNullClient();
        discordService.login();
    }

    @Test
    public void loginWhenNonNullClient() {
        mockNonNullClient();
        Mockito.when(discordClient.login()).thenReturn(mono);
        discordService.login();
    }

    public void mockNullClient() {
        Mockito.when(discordService.getDiscordClient()).thenReturn(null);
    }

    public void mockNonNullClient() {
        Mockito.when(discordService.getDiscordClient()).thenReturn(discordClient);
    }
}
