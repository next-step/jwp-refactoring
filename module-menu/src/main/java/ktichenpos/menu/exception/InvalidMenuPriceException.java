package ktichenpos.menu.exception;

public class InvalidMenuPriceException extends RuntimeException {
    public InvalidMenuPriceException() {
    }

    public InvalidMenuPriceException(String message) {
        super(message);
    }

    public InvalidMenuPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}
