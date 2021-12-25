package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;
    
    private OrderResponse() {
    }

    private OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }
    
    public static OrderResponse of(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new OrderResponse(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
    
    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), order.getOrderLineItems());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(orderTableId, that.orderTableId) && orderStatus == that.orderStatus && Objects.equals(orderedTime, that.orderedTime) && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
