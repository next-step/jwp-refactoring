package kitchenpos.domain.ordertable;

public class InvalidOrderTableException extends RuntimeException {
    public InvalidOrderTableException(final String message) {
        super(message);
    }
}
