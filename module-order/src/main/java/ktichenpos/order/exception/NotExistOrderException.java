package ktichenpos.order.exception;

public class NotExistOrderException extends RuntimeException {
    public NotExistOrderException() {
    }

    public NotExistOrderException(String message) {
        super(message);
    }

    public NotExistOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
