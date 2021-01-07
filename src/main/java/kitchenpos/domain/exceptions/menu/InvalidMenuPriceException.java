package kitchenpos.domain.exceptions.menu;

public class InvalidMenuPriceException extends RuntimeException {
    public InvalidMenuPriceException(final String message) {
        super(message);
    }
}
