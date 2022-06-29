package kitchenpos.Exception;

public class OrderStatusCompleteException extends RuntimeException {
    public OrderStatusCompleteException(String message) {
        super(message);
    }
}
