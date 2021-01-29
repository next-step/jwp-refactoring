package kitchenpos.order.exception;

public class InvalidChangeOrderStatusException extends RuntimeException {
    public InvalidChangeOrderStatusException(String message) {
        super(message);
    }
}
