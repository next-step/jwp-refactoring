package kitchenpos.domain.order.exceptions;

public class InvalidTryChangeOrderStatusException extends RuntimeException {
    public InvalidTryChangeOrderStatusException(final String message) {
        super(message);
    }
}
