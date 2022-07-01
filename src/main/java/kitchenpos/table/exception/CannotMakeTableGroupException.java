package kitchenpos.table.exception;

public class CannotMakeTableGroupException extends RuntimeException {
    public CannotMakeTableGroupException() {
    }

    public CannotMakeTableGroupException(String message) {
        super(message);
    }

    public CannotMakeTableGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
