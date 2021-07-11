package kitchenpos.order.event;

import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;

import java.util.List;

public class OrderCreatedEvent {

    private Order order;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderCreatedEvent(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        this.order = order;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Order getOrder() {
        return order;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
