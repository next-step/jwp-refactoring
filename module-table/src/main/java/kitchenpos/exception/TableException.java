package kitchenpos.exception;

public class TableException extends RuntimeException {
    public TableException(final TableExceptionType tableExceptionType) {
        super(tableExceptionType.getMessage());
    }

    public TableException(final String message) {
        super(message);
    }
}
