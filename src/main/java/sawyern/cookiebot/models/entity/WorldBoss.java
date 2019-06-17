package sawyern.cookiebot.models.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany
    @JoinColumn(name = "HAS_COOKIES")
    List<WorldBossHasCookie> hasCookies;
}
