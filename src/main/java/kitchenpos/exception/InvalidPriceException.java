package kitchenpos.exception;

public class InvalidPriceException extends IllegalArgumentException {
    public InvalidPriceException(String message) {
        super(message);
    }
}
