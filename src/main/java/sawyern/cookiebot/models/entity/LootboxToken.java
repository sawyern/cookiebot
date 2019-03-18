package sawyern.cookiebot.models.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "LOOTBOX_TOKEN")
@Data
@EqualsAndHashCode(callSuper = true)
public class LootboxToken extends DbItem {
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;
}
