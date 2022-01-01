package kitchenpos.core.exception;

public class IllegalProductPriceException extends IllegalArgumentException {
    public IllegalProductPriceException(String message) {
        super(message);
    }
}
