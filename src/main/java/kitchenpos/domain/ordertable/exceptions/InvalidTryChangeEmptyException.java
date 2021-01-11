package kitchenpos.domain.ordertable.exceptions;

public class InvalidTryChangeEmptyException extends RuntimeException {
    public InvalidTryChangeEmptyException(final String message) {
        super(message);
    }
}
