package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderLineItem.OrderLineItem;

import java.util.List;

public class OrderRequest {
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final List<OrderLineItem> orderLineItems;

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
