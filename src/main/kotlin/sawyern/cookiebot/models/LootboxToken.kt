package sawyern.cookiebot.models

import javax.persistence.*

@Entity
@Table(name = "TOKENS")
data class LootboxToken(
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    var account: Account,

    @ManyToOne
    @JoinColumn(name = "SEASON_ID")
    var season: Season
): DbItem()