package kitchenpos.order.event;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

public class OrderCreatedEvent {
    private Order order;
    private List<OrderLineItem> orderLineItems;

    public OrderCreatedEvent(Order order, List<OrderLineItem> orderLineItems) {
        this.order = order;
        this.orderLineItems = orderLineItems;
    }

    public Order getOrder() {
        return order;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
