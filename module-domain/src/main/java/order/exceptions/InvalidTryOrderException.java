package kitchenpos.domain.order.exceptions;

public class InvalidTryOrderException extends RuntimeException {
    public InvalidTryOrderException(final String message) {
        super(message);
    }
}
