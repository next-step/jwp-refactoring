package kitchenpos.menu.exception;

public class NotExistMenuProductException extends RuntimeException {
    public NotExistMenuProductException() {
    }

    public NotExistMenuProductException(String message) {
        super(message);
    }

    public NotExistMenuProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
