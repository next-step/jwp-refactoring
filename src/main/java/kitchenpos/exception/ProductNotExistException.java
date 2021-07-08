package kitchenpos.exception;

public class ProductNotExistException extends RuntimeException {
    public ProductNotExistException() {
    }

    public ProductNotExistException(String s) {
        super(s);
    }

    public ProductNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotExistException(Throwable cause) {
        super(cause);
    }
}
