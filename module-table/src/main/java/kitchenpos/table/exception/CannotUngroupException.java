package kitchenpos.table.exception;

public class CannotUngroupException extends RuntimeException {
    public CannotUngroupException() {
    }

    public CannotUngroupException(String message) {
        super(message);
    }

    public CannotUngroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
