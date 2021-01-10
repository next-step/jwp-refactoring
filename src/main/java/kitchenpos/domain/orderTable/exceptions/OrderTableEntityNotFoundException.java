package kitchenpos.domain.orderTable.exceptions;

public class OrderTableEntityNotFoundException extends RuntimeException {
    public OrderTableEntityNotFoundException(final String message) {
        super(message);
    }
}
