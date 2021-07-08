package kitchenpos.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException() {
    }

    public InvalidQuantityException(String s) {
        super(s);
    }

    public InvalidQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidQuantityException(Throwable cause) {
        super(cause);
    }
}
