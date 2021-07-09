package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.ArrayList;
import java.util.List;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private List<OrderLineItem> orderLineItems;

    public OrderResponse(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderLineItems());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return new ArrayList<>(orderLineItems);
    }
}
