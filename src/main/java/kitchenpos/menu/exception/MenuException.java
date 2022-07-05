package kitchenpos.menu.exception;

public class MenuException extends RuntimeException {
    public MenuException(final MenuExceptionType productExceptionType) {
        super(productExceptionType.getMessage());
    }

    public MenuException(final String message) {
        super(message);
    }
}
