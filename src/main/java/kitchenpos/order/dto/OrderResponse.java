package kitchenpos.order.dto;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderLineItem> orderLineItems;
    private final LocalDateTime orderedTime;
    private final Long orderTableId;
    private OrderStatus orderStatus;

    public OrderResponse(Orders order) {
        this.orderId = order.getId();
        this.orderLineItems = order.getOrderLineItems();
        this.orderStatus = order.getOrderStatus();
        this.orderedTime = order.getOrderedTime();
        this.orderTableId = order.getOrderTable().getId();
    }

    public Long getId() {
        return this.orderId;
    }

    public LocalDateTime getOrderedTime() {
        return this.orderedTime;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public List<OrderLineItem> getOrderLineItmes() {
        return this.orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }
}
