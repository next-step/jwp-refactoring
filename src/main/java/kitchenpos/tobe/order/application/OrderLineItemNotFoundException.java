package kitchenpos.tobe.order.application;

public class OrderLineItemNotFoundException extends RuntimeException {

    public OrderLineItemNotFoundException() {
    }

    public OrderLineItemNotFoundException(String message) {
        super(message);
    }
}
