package kitchenpos.order.exception;

public class CannotOrderException extends RuntimeException {

    public CannotOrderException(String message) {
        super(message);
    }
}
