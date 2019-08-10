package sawyern.cookiebot.constants

class Constants {
    companion object {
        const val SEASON_ACTIVE: String = "Active"
        const val SEASON_INACTIVE: String = "Inactive"
    }

    object Time {
        const val TIME_ZONE_EST = "America/New_York"
        const val RESET_TIME = "0 0 20 ? * MON"
    }

    object Cookie {
        const val STARTING_COOKIES: Int = 10
        const val WEEKLY_LOOTBOXES: Int = 3
    }

    object PrestigeMultiplier {
        const val FIRST_PLACE = 1.15
        const val SECOND_PLACE = 1.10
        const val THIRD_PLACE = 1.05
    }
}