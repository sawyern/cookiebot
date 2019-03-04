package sawyern.cookiebot.models.exception;

import org.springframework.http.HttpStatus;

public class CookieException extends Exception {

    private HttpStatus httpStatus;

    public CookieException() {
        this(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public CookieException(String msg) {
        super(msg);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    public CookieException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
    public CookieException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
