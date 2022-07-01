package kitchenpos.table.exception;

public class NotExistTableException extends RuntimeException{
    public NotExistTableException() {
    }

    public NotExistTableException(String message) {
        super(message);
    }

    public NotExistTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
