package kitchenpos.domain.ordertable.exceptions;

public class OrderTableEntityNotFoundException extends RuntimeException {
    public OrderTableEntityNotFoundException(final String message) {
        super(message);
    }
}
