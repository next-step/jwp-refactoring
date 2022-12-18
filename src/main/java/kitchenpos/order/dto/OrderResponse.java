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

    public OrderResponse(Long id, List<OrderLineItem> orderLineItems, LocalDateTime orderedTime, OrderStatus orderStatus, Long orderTableId) {
        this.orderId = id;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTableId = orderTableId;
    }

    public static OrderResponse of(Orders orders) {
        return new OrderResponse(orders.getId(), orders.getOrderLineItems(), orders.getOrderedTime(), orders.getOrderStatus(), orders.getOrderTable().getId());
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

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }
}
