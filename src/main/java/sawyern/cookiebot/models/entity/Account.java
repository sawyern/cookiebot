package sawyern.cookiebot.models.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Account")
public class Account extends DbItem {
    private String discordId;

    public Account() {}

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }
}
