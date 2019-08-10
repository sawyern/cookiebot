package sawyern.cookiebot.constants

import java.util.*

class CommandConstants {
    companion object {
        const val COMMAND_START = "!"
        const val SPACE = " "

        val RANDOM = Random()

        const val COMMAND_DELIM = " "

        const val QUOTE = "\""
        const val EMPTY_STRING = ""

        const val QUOTE_REGEX = "([^\"]\\S*|\".+?\")\\s*"
        const val DICE = "\uD83C\uDFB2"
    }

    object CommandName {
        const val PING = "ping"
        const val HELP = "help"
        const val REGISTER = "register"
        const val COOKIES = "cookies"
        const val UNKNOWN = "unknown"
    }
}