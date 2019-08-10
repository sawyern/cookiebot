package sawyern.cookiebot.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "ACCOUNTS")
data class Account(
        @Column
        var discordId: String,

        @Column
        var username: String
): DbItem()