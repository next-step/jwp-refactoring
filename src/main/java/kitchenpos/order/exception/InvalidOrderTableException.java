package kitchenpos.order.exception;

public class InvalidOrderTableException extends RuntimeException {
    public InvalidOrderTableException() {
    }

    public InvalidOrderTableException(String message) {
        super(message);
    }
}
