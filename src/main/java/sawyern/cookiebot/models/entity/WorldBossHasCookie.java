package sawyern.cookiebot.models.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "WORLD_BOSS_HAS_COOKIE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class WorldBossHasCookie extends DbItem {
    @JoinColumn(name = "DISCORD_ID")
    @OneToOne
    private Account account;

    @JoinColumn(name = "WORLD_BOSS")
    @OneToOne
    private WorldBoss worldBoss;

    @Column(name = "COOKIES_FED")
    private int cookiesFed;
}
