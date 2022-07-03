package kitchenpos.common.exception;

public class ErrorResponse {
    private String message;

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message);
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
