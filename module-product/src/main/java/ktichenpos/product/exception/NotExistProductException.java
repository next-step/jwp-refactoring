package ktichenpos.product.exception;

public class NotExistProductException extends RuntimeException {
    public NotExistProductException() {
    }

    public NotExistProductException(String message) {
        super(message);
    }

    public NotExistProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
