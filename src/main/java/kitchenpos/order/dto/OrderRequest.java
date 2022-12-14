package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {

    private long orderTableId;
    private List<OrderLineItem> orderLineItems;

    protected OrderRequest() {
    }

    private OrderRequest(long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(long orderTableId, List<OrderLineItem> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public Order createOrder(OrderTable orderTable) {
        return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), getOrderLineItems());
    }
}
