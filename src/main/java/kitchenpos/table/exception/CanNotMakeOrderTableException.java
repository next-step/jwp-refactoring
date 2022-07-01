package kitchenpos.table.exception;

/**
 * Can not make an order to the table
 */
public class CanNotMakeOrderTableException extends RuntimeException{
    public CanNotMakeOrderTableException() {
    }

    public CanNotMakeOrderTableException(String message) {
        super(message);
    }

    public CanNotMakeOrderTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
