package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderResponse {
    private Long id;

    private Long orderTableId;

    private OrderStatus orderStatus;

    private List<OrderLineItem> orderLineItems;

    private LocalDateTime orderedTime;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        this.orderedTime = orderedTime;
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus();
        this.orderLineItems = order.getOrderLineItems();
        this.orderedTime = order.getOrderedTime();
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderResponse)) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOrderTableId(), that.getOrderTableId()) && getOrderStatus() == that.getOrderStatus() && Objects.equals(getOrderLineItems(), that.getOrderLineItems()) && Objects.equals(getOrderedTime(), that.getOrderedTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderTableId(), getOrderStatus(), getOrderLineItems(), getOrderedTime());
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", orderTableId=" + orderTableId +
                ", orderStatus=" + orderStatus +
                ", orderLineItems=" + orderLineItems +
                ", orderedTime=" + orderedTime +
                '}';
    }
}
