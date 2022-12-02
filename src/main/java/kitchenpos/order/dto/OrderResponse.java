package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderLineItem> orderLineItems;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.orderLineItems = order.getOrderLineItems();
    }

    public Long getId() {
        return this.orderId;
    }
}
