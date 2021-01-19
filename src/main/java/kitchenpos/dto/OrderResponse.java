package kitchenpos.dto;

import kitchenpos.domain.Orders;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderResponse {
    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    public static OrderResponse from(Orders orders) {
        return new OrderResponse(orders.getId(), orders.getOrderTableId(), orders.getOrderStatus(), orders.getOrderedTime(), orders.getOrderLineItems());
    }

    private OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
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
        return Objects.equals(id, that.id) && Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderStatus, that.orderStatus) && Objects.equals(orderedTime, that.orderedTime) && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", orderTableId=" + orderTableId +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderedTime=" + orderedTime +
                ", orderLineItems=" + orderLineItems +
                '}';
    }
}
