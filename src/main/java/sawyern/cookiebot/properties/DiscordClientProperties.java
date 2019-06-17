package sawyern.cookiebot.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "discord")
@Getter
@Setter
@NoArgsConstructor
public class DiscordClientProperties {
    private String token;
    private String botChannelId;
}
