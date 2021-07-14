package kitchenpos.table.exception;

public class CannotChangeTableEmptyException extends RuntimeException {

    public CannotChangeTableEmptyException(String message) {
        super(message);
    }
}
