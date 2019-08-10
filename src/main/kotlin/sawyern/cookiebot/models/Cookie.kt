package sawyern.cookiebot.models

import javax.persistence.*

@Entity
@Table(name = "COOKIES")
data class Cookie(
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    var account: Account,

    @ManyToOne
    @JoinColumn(name = "SEASON_ID")
    var season: Season
): DbItem()