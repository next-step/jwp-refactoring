package kitchenpos.common.exception;

public class InvalidPriceException extends IllegalArgumentException {
    public InvalidPriceException() {
    }

    public InvalidPriceException(String s) {
        super(s);
    }
}
