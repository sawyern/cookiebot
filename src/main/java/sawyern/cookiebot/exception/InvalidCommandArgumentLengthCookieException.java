package sawyern.cookiebot.exception;

import java.text.MessageFormat;
import java.util.List;

public class InvalidCommandArgumentLengthCookieException extends CookieException {
    public InvalidCommandArgumentLengthCookieException(List<Integer> allowedNumArgs) {
        super(MessageFormat.format("Invalid number of arguments. Expected {0}. See !help for more information.", allowedNumArgs));
    }
}
