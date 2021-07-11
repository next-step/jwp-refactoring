package kitchenpos.common.exception;

public class MenuCheapException extends RuntimeException {
    public MenuCheapException() {
    }

    public MenuCheapException(String s) {
        super(s);
    }

    public MenuCheapException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuCheapException(Throwable cause) {
        super(cause);
    }
}
