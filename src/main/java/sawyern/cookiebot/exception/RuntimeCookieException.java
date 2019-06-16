package sawyern.cookiebot.exception;

public class RuntimeCookieException extends RuntimeException {
    public RuntimeCookieException() {
    }

    public RuntimeCookieException(String message) {
        super(message);
    }

    public RuntimeCookieException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeCookieException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RuntimeCookieException(Throwable cause) {
        super(cause);
    }
}
