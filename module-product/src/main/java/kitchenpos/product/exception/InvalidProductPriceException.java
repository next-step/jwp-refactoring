package kitchenpos.product.exception;

public class InvalidProductPriceException extends RuntimeException {
    public InvalidProductPriceException() {
    }

    public InvalidProductPriceException(String message) {
        super(message);
    }

    public InvalidProductPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}
