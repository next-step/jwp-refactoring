package kitchenpos.menu.exception;

public class NotExistMenuException extends RuntimeException{
    public NotExistMenuException() {
    }

    public NotExistMenuException(String message) {
        super(message);
    }

    public NotExistMenuException(String message, Throwable cause) {
        super(message, cause);
    }
}
