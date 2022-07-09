package kitchenpos.exception;

public class MenuException extends RuntimeException {
    public MenuException(final MenuExceptionType menuExceptionType) {
        super(menuExceptionType.getMessage());
    }

    public MenuException(final String message) {
        super(message);
    }
}
