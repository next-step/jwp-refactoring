package kitchenpos.domain.product;

public class InvalidProductPriceException extends RuntimeException {
    public InvalidProductPriceException(final String message) {
        super(message);
    }
}
