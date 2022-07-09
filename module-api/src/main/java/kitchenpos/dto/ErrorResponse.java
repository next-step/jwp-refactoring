package kitchenpos.dto;

public class ErrorResponse {

    private String message;
    private int status;

    public ErrorResponse() {
    }

    public ErrorResponse(final String message, final int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
