package kitchenpos.order.exception;

public class CannotChangeOrderStatusException  extends RuntimeException{
    public CannotChangeOrderStatusException() {
    }

    public CannotChangeOrderStatusException(String message) {
        super(message);
    }

    public CannotChangeOrderStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
