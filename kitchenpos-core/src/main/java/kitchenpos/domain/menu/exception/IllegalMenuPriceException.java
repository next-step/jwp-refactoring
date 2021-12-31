package kitchenpos.domain.menu.exception;

public class IllegalMenuPriceException extends IllegalArgumentException {
    public IllegalMenuPriceException(String message) {
        super(message);
    }
}
