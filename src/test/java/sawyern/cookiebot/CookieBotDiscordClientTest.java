package sawyern.cookiebot;

import discord4j.core.DiscordClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sawyern.cookiebot.commands.BotCommand;
import sawyern.cookiebot.properties.DiscordClientProperties;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class CookieBotDiscordClientTest {
    @InjectMocks
    private CookieBotDiscordClient cookieBotDiscordClient;

    @Mock
    private DiscordClient discordClient;
    @Mock
    private DiscordClientProperties discordClientProperties;
    @Mock
    private List<BotCommand> botCommands = new ArrayList<>();

    @Before
    public void setup() {
        Mockito.when(discordClientProperties.getToken()).thenReturn(TestConstants.DISCORD_TOKEN);
    }

    @Test
    public void startDiscordClient() {
        cookieBotDiscordClient.startDiscordClient();
    }
}
