package kitchenpos.domain.product.exceptions;

public class InvalidProductPriceException extends RuntimeException {
    public InvalidProductPriceException(final String message) {
        super(message);
    }
}
