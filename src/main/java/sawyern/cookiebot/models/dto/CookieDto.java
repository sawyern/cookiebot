package sawyern.cookiebot.models.dto;

public class CookieDto {
    private String discordId;
    private int numCookies;

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public int getNumCookies() {
        return numCookies;
    }

    public void setNumCookies(int numCookies) {
        this.numCookies = numCookies;
    }
}
