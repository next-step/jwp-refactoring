package kitchenpos.ordertable.exception;

public class CannotChangeEmptyException extends RuntimeException {
    public CannotChangeEmptyException(String message) {
        super(message);
    }
}
