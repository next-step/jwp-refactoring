package kitchenpos.common.exception;

public class InvalidNameException extends RuntimeException {
    public InvalidNameException() {
    }

    public InvalidNameException(String s) {
        super(s);
    }

    public InvalidNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNameException(Throwable cause) {
        super(cause);
    }
}
