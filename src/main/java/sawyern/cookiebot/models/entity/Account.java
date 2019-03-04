package sawyern.cookiebot.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT")
public class Account extends DbItem {
    @Column(name = "discordId")
    private String discordId;

    public Account() {
        super();
    }
    public Account(String discordId) {
        super();
        this.discordId = discordId;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }
}
