package sawyern.cookiebot.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "LOOTBOX_TOKEN")
public class LootboxToken extends DbItem {
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
