package kitchenpos.order.domain;

public class OrderGeneratedEvent {
    private Order order;

    public OrderGeneratedEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
