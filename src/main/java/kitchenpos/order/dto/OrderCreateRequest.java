package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItem> orderLineItems;

    public OrderCreateRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public OrderLineItems toOrderLineItems() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(this.orderLineItems);
        return orderLineItems;
    }
}
