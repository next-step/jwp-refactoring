package kitchenpos.exception;

public class TableEmptyException extends IllegalArgumentException {
    public TableEmptyException() {
    }

    public TableEmptyException(String s) {
        super(s);
    }

    public TableEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public TableEmptyException(Throwable cause) {
        super(cause);
    }
}
