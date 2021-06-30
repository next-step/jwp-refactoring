package kitchenpos.exception;

public class MenuCheapException extends IllegalArgumentException {
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
