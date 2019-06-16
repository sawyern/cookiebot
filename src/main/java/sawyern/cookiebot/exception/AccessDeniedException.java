package sawyern.cookiebot.exception;

public class AccessDeniedException extends RuntimeException {
    private final StringBuilder trace = new StringBuilder();

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, String traceMessage, Throwable cause) {
        super(message, cause);
        if (traceMessage != null)
            this.trace.append(traceMessage);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

    public String getTrace() {
        return this.trace.toString();
    }
}
