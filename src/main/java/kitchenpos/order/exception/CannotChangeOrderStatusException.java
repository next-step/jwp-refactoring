package kitchenpos.order.exception;

public class CannotChangeOrderStatusException extends RuntimeException {

    public CannotChangeOrderStatusException(String message) {
        super(message);
    }
}
