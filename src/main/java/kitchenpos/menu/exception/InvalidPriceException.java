package kitchenpos.menu.exception;

public class InvalidPriceException extends RuntimeException {
    private static final String message = "가격은 0보다 커야 합니다.";

    public InvalidPriceException() {
        super(message);
    }
}
