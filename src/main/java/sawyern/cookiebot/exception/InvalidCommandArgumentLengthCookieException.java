package sawyern.cookiebot.exception;

import sawyern.cookiebot.constants.CommandConstants;

import java.text.MessageFormat;
import java.util.Set;

public class InvalidCommandArgumentLengthCookieException extends CookieException {
    public static String getMessageIfSet(Set<Integer> allowedNumArgs) {
        StringBuilder builder = new StringBuilder("[");
        allowedNumArgs.forEach(arg -> builder.append(arg).append(CommandConstants.SPACE));
        builder.deleteCharAt(builder.length() - 1).append("]");
        return MessageFormat.format("Invalid number of arguments. Expected {0}. See !help for more information.", builder);
    }

    public InvalidCommandArgumentLengthCookieException(String message) {
        super(message);
    }
}
