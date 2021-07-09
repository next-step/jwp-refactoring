package kitchenpos.exception;

public class OrderNotCompletedException extends RuntimeException {
    public OrderNotCompletedException(String message) {
        super(message);
    }
}
