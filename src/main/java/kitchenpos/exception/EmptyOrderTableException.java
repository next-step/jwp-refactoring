package kitchenpos.exception;

public class EmptyOrderTableException extends RuntimeException {
    public EmptyOrderTableException(String message) {
        super(message);
    }
}
