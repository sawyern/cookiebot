package sawyern.cookiebot.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AssertMessage {
    public static final String EMPTY_MESSAGE = "Cannot parse empty message";
    public static final String DISCORD_CLIENT_NOT_SET = "Discord client not yet set!";

    public static final String DISCORD_TOKEN_NOT_SET = "Discord token was not provided in properties file";
    public static final String ERROR_LOADING_PROPERTY = "Error loading property";

}
