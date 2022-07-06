package kitchenpos.exception;

public class OrderStatusCompleteException extends RuntimeException {
    public OrderStatusCompleteException(String message) {
        super(message);
    }
}
