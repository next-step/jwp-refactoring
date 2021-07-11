package kitchenpos.order.application;

import kitchenpos.order.domain.Order;

public class OrderGeneratedEvent {
    private Order order;

    public OrderGeneratedEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
