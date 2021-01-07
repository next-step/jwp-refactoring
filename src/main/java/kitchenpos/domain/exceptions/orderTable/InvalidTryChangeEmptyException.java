package kitchenpos.domain.exceptions.orderTable;

public class InvalidTryChangeEmptyException extends RuntimeException {
    public InvalidTryChangeEmptyException(final String message) {
        super(message);
    }
}
