package sawyern.cookiebot.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorInfo {
    private String code;
    private String details;
    private List<String> errors;
    private ErrorType type;
    private String userMessage;

    public ErrorInfo(ErrorType type, String userMessage) {
        this.type = type;
        this.userMessage = userMessage;
    }
}
