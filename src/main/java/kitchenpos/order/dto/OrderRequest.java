package kitchenpos.order.dto;

import java.util.List;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItem> orderLineItems;
    
    private OrderRequest() {
    }

    private OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }
    
    public static OrderRequest of(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItems);
    }
    
    public static OrderRequest from(Order order) {
        return new OrderRequest(order.getOrderTableId(), order.getOrderStatus(), order.getOrderLineItems());
    }
    
    public Order toOrder(OrderTable orderTable) {
        return Order.of(orderTable, orderStatus);
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
