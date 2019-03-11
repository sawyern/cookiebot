package sawyern.cookiebot.models.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "WEEKLY_COOLDOWN")
public class WeeklyCooldown extends DbItem {
    @OneToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column
    private LocalDateTime canUseNext;

    public WeeklyCooldown() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getCanUseNext() {
        return canUseNext;
    }

    public void setCanUseNext(LocalDateTime canUseNext) {
        this.canUseNext = canUseNext;
    }
}
