package sawyern.cookiebot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sawyern.cookiebot.services.DiscordService;

@RunWith(SpringJUnit4ClassRunner.class)
public class CookieBotDiscordClientTest {
    @InjectMocks
    private CookieBotDiscordClient cookieBotDiscordClient;

    @Mock
    private DiscordService discordService;

    @Test
    public void startDiscordClient() {
        cookieBotDiscordClient.startDiscordClient();
    }
}
