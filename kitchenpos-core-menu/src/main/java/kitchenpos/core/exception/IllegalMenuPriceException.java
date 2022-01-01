package kitchenpos.core.exception;

public class IllegalMenuPriceException extends IllegalArgumentException {
    public IllegalMenuPriceException(String message) {
        super(message);
    }
}
