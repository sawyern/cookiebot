package sawyern.cookiebot.exception

import sawyern.cookiebot.constants.CommandConstants

class InvalidCommandArgumentLengthCookieException(private val allowedNumArgs: Set<Int>) : CookieException("") {
    override val message: String?
        get() {
            val builder = StringBuilder("[")
            allowedNumArgs.forEach { arg -> builder.append(arg).append(CommandConstants.SPACE) }
            builder.deleteCharAt(builder.length - 1).append("]")
            return "Invalid number of arguments. Expected $builder. See !help for more information."
        }
}