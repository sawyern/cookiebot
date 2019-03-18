package sawyern.cookiebot.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiveCookieDto {
    private String senderId;
    private String recieverId;
    private int numCookies;
}
