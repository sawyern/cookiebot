package sawyern.cookiebot.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "SEASONS")
data class Season(
        @Column
        var name: String,

        @Column
        var status: String
): DbItem()