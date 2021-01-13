package kitchenpos.domain.order.exceptions;

public class OrderStatusNotFoundException extends RuntimeException {
    public OrderStatusNotFoundException(final String message) {
        super(message);
    }
}
