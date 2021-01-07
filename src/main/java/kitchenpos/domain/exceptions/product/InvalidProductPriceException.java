package kitchenpos.domain.exceptions.product;

public class InvalidProductPriceException extends RuntimeException {
    public InvalidProductPriceException(final String message) {
        super(message);
    }
}
