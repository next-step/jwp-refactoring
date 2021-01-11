package kitchenpos.domain.menu.exceptions;

public class InvalidMenuPriceException extends RuntimeException {
    public InvalidMenuPriceException(final String message) {
        super(message);
    }
}
