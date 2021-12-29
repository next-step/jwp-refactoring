package kitchenpos.exception;

public class InvalidOrderTableException extends IllegalArgumentException {
    public InvalidOrderTableException(String message) {
        super(message);
    }
}
