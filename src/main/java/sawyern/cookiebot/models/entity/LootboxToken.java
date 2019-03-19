package sawyern.cookiebot.models.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "LOOTBOX_TOKEN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class LootboxToken extends DbItem {
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;
}
