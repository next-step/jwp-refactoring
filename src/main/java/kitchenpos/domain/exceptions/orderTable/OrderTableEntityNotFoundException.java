package kitchenpos.domain.exceptions.orderTable;

public class OrderTableEntityNotFoundException extends RuntimeException {
    public OrderTableEntityNotFoundException(final String message) {
        super(message);
    }
}
