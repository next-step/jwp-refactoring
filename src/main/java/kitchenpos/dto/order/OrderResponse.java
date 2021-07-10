package kitchenpos.dto.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderResponse {

    private final Long id;
    private final OrderTable orderTable;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    public OrderResponse(Long id, OrderTable orderTable, OrderStatus orderStatus,
                         LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTable(), order.getOrderStatus(),
                order.getOrderedTime(), order.getOrderLineItems());
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
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
        return Objects.equals(id, that.id) && Objects.equals(orderTable, that.orderTable) && orderStatus == that.orderStatus && Objects.equals(orderedTime, that.orderedTime) && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", orderTable=" + orderTable +
                ", orderStatus=" + orderStatus +
                ", orderedTime=" + orderedTime +
                ", orderLineItems=" + orderLineItems +
                '}';
    }
}
