package kitchenpos.domain.exceptions.order;

public class InvalidTryOrderException extends RuntimeException {
    public InvalidTryOrderException(final String message) {
        super(message);
    }
}
