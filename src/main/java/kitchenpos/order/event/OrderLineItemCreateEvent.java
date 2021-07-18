package kitchenpos.order.event;

import java.util.List;

import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemCreateEvent {
    private final Order order;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderLineItemCreateEvent(final Order order, final List<OrderLineItemRequest> orderLineItems) {
        this.order = order;
        this.orderLineItems = orderLineItems;
    }

    public Order getOrder() {
        return order;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
