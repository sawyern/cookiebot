package sawyern.cookiebot.bot.exception;

public class InvalidMessageCookieException extends CookieException {
    public InvalidMessageCookieException() {
        super("Invalid message parameter. Failed to parse arguments.");
    }
}
