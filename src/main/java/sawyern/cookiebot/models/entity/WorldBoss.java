package sawyern.cookiebot.models.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WORLD_BOSS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class WorldBoss extends DbItem {
    @Column(name = "IS_DEAD")
    boolean isDead;

    @Column(name = "SPAWN_TIME")
    LocalDateTime spawnTime;

    @Column(name = "TYPE")
    String type;

    @OneToOne
    @JoinColumn(name = "ACCOUNT_ID")
    Account lastFed;
}
