package kitchenpos.dto.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderLineItem.OrderLineItem;
import kitchenpos.domain.orderTable.OrderTable;

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

    public Order toOrder(OrderTable orderTable) {
        return new Order(null, orderTable, orderStatus, orderLineItems);
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
