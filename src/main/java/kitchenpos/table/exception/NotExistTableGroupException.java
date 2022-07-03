package kitchenpos.table.exception;

public class NotExistTableGroupException extends RuntimeException {
    public NotExistTableGroupException() {
    }

    public NotExistTableGroupException(String message) {
        super(message);
    }

    public NotExistTableGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
