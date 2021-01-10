package kitchenpos.domain.orderTable.exceptions;

public class InvalidTryChangeEmptyException extends RuntimeException {
    public InvalidTryChangeEmptyException(final String message) {
        super(message);
    }
}
