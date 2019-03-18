package sawyern.cookiebot.models.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WEEKLY_COOLDOWN")
@Data
@EqualsAndHashCode(callSuper = true)
public class WeeklyCooldown extends DbItem {
    @OneToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column
    private LocalDateTime canUseNext;
}
