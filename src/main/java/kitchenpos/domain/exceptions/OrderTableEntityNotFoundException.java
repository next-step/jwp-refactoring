package kitchenpos.domain.exceptions;

public class OrderTableEntityNotFoundException extends RuntimeException {
    public OrderTableEntityNotFoundException(final String message) {
        super(message);
    }
}
