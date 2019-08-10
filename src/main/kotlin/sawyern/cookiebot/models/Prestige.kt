package sawyern.cookiebot.models

import javax.persistence.*

@Entity
@Table(name = "PRESTIGE")
data class Prestige(
        @OneToOne
        @JoinColumn(name = "ACCOUNT_ID")
        var account: Account,

        @Column(name = "VALUE")
        var value: Int
): DbItem()