package sawyern.cookiebot.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT")
public class Account extends DbItem {
    @Column(name = "discordId")
    private String discordId;

    @Column(name = "username")
    private String username;

    public Account() {
        super();
    }

    public Account(String discordId, String username) {
        super();
        this.discordId = discordId;
        this.username = username;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
