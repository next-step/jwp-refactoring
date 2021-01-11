package kitchenpos.domain.orderTable;

public class InvalidOrderTableException extends RuntimeException {
    public InvalidOrderTableException(final String message) {
        super(message);
    }
}
