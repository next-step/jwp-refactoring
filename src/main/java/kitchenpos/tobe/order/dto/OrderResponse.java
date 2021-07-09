package kitchenpos.tobe.order.dto;

import kitchenpos.tobe.order.domain.Order;
import kitchenpos.tobe.order.domain.OrderLineItem;
import kitchenpos.tobe.order.domain.OrderStatus;
import kitchenpos.tobe.table.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderResponse {
    private Long id;

    private OrderTable orderTable;

    private OrderStatus orderStatus;

    private List<OrderLineItem> orderLineItems;

    private LocalDateTime orderedTime;

    public OrderResponse() {
    }

    public OrderResponse(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        this.orderedTime = orderedTime;
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderTable = order.getOrderTable();
        this.orderStatus = order.getOrderStatus();
        this.orderLineItems = order.getOrderLineItems();
        this.orderedTime = order.getOrderedTime();
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
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOrderTable(),
                that.getOrderTable()) && getOrderStatus() == that.getOrderStatus() &&
                Objects.equals(getOrderLineItems(), that.getOrderLineItems()) &&
                Objects.equals(getOrderedTime(), that.getOrderedTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderTable(), getOrderStatus(), getOrderLineItems(), getOrderedTime());
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", orderTable=" + orderTable +
                ", orderStatus=" + orderStatus +
                ", orderLineItems=" + orderLineItems +
                ", orderedTime=" + orderedTime +
                '}';
    }
}
