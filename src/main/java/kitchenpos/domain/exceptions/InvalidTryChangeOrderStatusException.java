package kitchenpos.domain.exceptions;

public class InvalidTryChangeOrderStatusException extends RuntimeException {
    public InvalidTryChangeOrderStatusException(final String message) {
        super(message);
    }
}
