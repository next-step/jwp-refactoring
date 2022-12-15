package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderLineItem> orderLineItems;
    private final LocalDateTime orderedTime;
    private final Long orderTableId;
    private String status;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.orderLineItems = order.getOrderLineItems();
        this.status = order.getOrderStatus();
        this.orderedTime = order.getOrderedTime();
        this.orderTableId = order.getOrderTable().getId();
    }

    public Long getId() {
        return this.orderId;
    }

    public String getStatus() {
        return this.status;
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
}
