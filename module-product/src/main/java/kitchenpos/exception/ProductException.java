package kitchenpos.exception;

public class ProductException extends RuntimeException {
    public ProductException(final ProductExceptionType productExceptionType) {
        super(productExceptionType.getMessage());
    }

    public ProductException(final String message) {
        super(message);
    }
}
