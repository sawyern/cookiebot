package sawyern.cookiebot.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import sawyern.cookiebot.models.dto.ErrorInfo;
import sawyern.cookiebot.models.dto.ErrorType;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Component
@Slf4j
public class GlobalExceptionHandler {
    public static final String ERR_MSG_INVALID_ARG = "Invalid Argument";

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorInfo handleAccessDenied(final AccessDeniedException e) {
        final ErrorInfo err = new ErrorInfo(ErrorType.ERROR, e.getMessage());
        err.setDetails((e.getTrace() != null) ? e.getTrace() : e.getMessage());
        return err;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorInfo handleConstraintViolationException(final ConstraintViolationException e) {
        final List<String> errors = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        final ErrorInfo errorInfo = new ErrorInfo(ErrorType.ERROR, StringUtils.collectionToDelimitedString(errors, ", "));
        errorInfo.setDetails("Constraint Violation");
        errorInfo.setErrors(errors);
        return errorInfo;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleException(final Exception e) {
        log.error(e.getMessage(), e);
        final ErrorInfo errorInfo = new ErrorInfo(ErrorType.ERROR, e.getMessage());
        if (e.getCause() != null)
            errorInfo.setDetails(e.getCause().getMessage());
        return errorInfo;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorInfo handleIllegalArgumentException(final IllegalArgumentException e) {
        final ErrorInfo errorInfo = new ErrorInfo(ErrorType.ERROR, ERR_MSG_INVALID_ARG);
        errorInfo.setCode(HttpStatus.BAD_REQUEST.toString());
        errorInfo.setDetails(e.getMessage());
        return errorInfo;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerException.class)
    public ErrorInfo handleInternalServerException(final InternalServerException e) {
        log.error(e.getMessage(), e);
        final ErrorInfo errorInfo = new ErrorInfo(ErrorType.ERROR, e.getMessage());
        errorInfo.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        if (e.getCause() != null)
            errorInfo.setDetails(e.getCause().getMessage());
        return errorInfo;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final ErrorInfo errorInfo = new ErrorInfo(ErrorType.ERROR, ERR_MSG_INVALID_ARG);
        errorInfo.setCode(HttpStatus.BAD_REQUEST.toString());
        errorInfo.setDetails(ERR_MSG_INVALID_ARG);
        errorInfo.setErrors(e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList()));
        return errorInfo;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ErrorInfo handleNotFound(final ObjectNotFoundException e) {
        final ErrorInfo errorInfo = new ErrorInfo(ErrorType.ERROR, e.getMessage());
        errorInfo.setDetails(e.getMessage());
        return errorInfo;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnavailableException.class)
    public ErrorInfo handleServiceUnavailableException(final ServiceUnavailableException e) {
        final ErrorInfo errorInfo = new ErrorInfo(ErrorType.ERROR, e.getMessage());
        errorInfo.setDetails(e.getMessage());
        return errorInfo;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorInfo handleUnauthorizedException(final UnauthorizedException e) {
        final ErrorInfo errorInfo = new ErrorInfo(ErrorType.ERROR, e.getMessage());
        errorInfo.setCode(HttpStatus.UNAUTHORIZED.toString());
        errorInfo.setDetails(e.getMessage());
        return errorInfo;
    }
}
