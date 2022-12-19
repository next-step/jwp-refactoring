package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderLineItemRequest;

import java.util.List;

public class OrderCreatedEvent {

    private List<OrderLineItemRequest> orderLineItemRequests;
    private Order order;

    public OrderCreatedEvent(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        this.orderLineItemRequests = orderLineItemRequests;
        this.order = order;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public Order getOrder() {
        return order;
    }
}
