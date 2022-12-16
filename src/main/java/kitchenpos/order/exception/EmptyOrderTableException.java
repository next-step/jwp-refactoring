package kitchenpos.order.exception;

public class EmptyOrderTableException extends RuntimeException {
    public EmptyOrderTableException(String message) {
        super(message);
    }
}
