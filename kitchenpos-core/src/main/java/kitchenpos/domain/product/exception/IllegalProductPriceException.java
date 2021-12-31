package kitchenpos.domain.product.exception;

public class IllegalProductPriceException extends IllegalArgumentException {
    public IllegalProductPriceException(String message) {
        super(message);
    }
}
