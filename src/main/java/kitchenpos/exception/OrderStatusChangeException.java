package kitchenpos.exception;

public class OrderStatusChangeException extends RuntimeException {
    public OrderStatusChangeException(String message) {
        super(message);
    }
}
