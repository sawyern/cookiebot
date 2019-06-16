package sawyern.cookiebot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sawyern.cookiebot.services.DiscordService;

@Component
@Slf4j
@RequiredArgsConstructor
public class CookieBotDiscordClient {
    private final DiscordService discordService;

    @EventListener(ApplicationReadyEvent.class)
    public void startDiscordClient() {
        discordService.setDiscordClient();
        discordService.subscribeReady();
        discordService.subscribeAllCommands();
        discordService.login();
    }
}
