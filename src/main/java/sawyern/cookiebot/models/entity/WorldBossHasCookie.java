package sawyern.cookiebot.models.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WORLD_BOSS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class WorldBossHasCookie extends DbItem {
    @Column(name = "DISCORD_ID")
    private Account account;

    @Column(name = "WORLD_BOSS")
    private WorldBoss worldBoss;

    @Column(name = "COOKIES_FED")
    private int cookiesFed;
}
