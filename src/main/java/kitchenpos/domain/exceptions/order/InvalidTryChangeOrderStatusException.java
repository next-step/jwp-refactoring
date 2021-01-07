package kitchenpos.domain.exceptions.order;

public class InvalidTryChangeOrderStatusException extends RuntimeException {
    public InvalidTryChangeOrderStatusException(final String message) {
        super(message);
    }
}
