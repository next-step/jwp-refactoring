package kitchenpos.product.exception;

public abstract class CustomException extends RuntimeException {
    protected CustomException(final String message) {
        super(message);
    }
}
