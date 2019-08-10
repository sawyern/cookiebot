package sawyern.cookiebot.exception

class AssertMessage {
    companion object {
        val EMPTY_MESSAGE = "Cannot parse empty message"
        val DISCORD_CLIENT_NOT_SET = "Discord client not yet set!"

        val DISCORD_TOKEN_NOT_SET = "Discord token was not provided in properties file"
        val NULL_BOT_COMMANDS = "Error initializing bot command list"
        val ERROR_LOADING_PROPERTY = "Error loading property"
    }
}