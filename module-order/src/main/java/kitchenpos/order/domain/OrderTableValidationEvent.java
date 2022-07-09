package kitchenpos.order.domain;

public class OrderTableValidationEvent {
    private final Order order;

    public OrderTableValidationEvent(final Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
