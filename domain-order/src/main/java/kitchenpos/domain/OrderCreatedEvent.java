package kitchenpos.domain;

public class OrderCreatedEvent {
    private final Order order;

    public OrderCreatedEvent(Order order) {
        this.order = order;
    }

    public Long getOrderTableId() {
        return order.getOrderTableId();
    }
}
