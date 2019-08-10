package sawyern.cookiebot.models

import javax.persistence.*

@Entity
@Table(name = "WEEKLY_COOLDOWNS")
data class WeeklyCooldown(
    @OneToOne
    @JoinColumn(name = "ACCOUNT_ID")
    var account: Account,

    @Column
    var onCooldown: Boolean
): DbItem()