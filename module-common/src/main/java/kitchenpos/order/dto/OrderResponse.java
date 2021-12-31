package kitchenpos.order.dto;

import java.time.*;
import java.util.*;

import kitchenpos.order.domain.*;
import kitchenpos.table.domain.*;

public class OrderResponse {

    public Long id;
    public OrderTable orderTable;
    public OrderStatus orderStatus;
    public List<OrderLineItem> orderLineItems;
    public LocalDateTime orderedTime;

    public OrderResponse() {
    }

    public OrderResponse(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems.getOrderLineItems();
        this.orderedTime = orderedTime;
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderTable = order.getOrderTable();
        this.orderStatus = order.getOrderStatus();
        this.orderLineItems = order.getOrderLineItems().getOrderLineItems();
        this.orderedTime = order.getOrderedTime();
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(order);
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(orderTable, that.orderTable) && orderStatus == that.orderStatus && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTable, orderStatus, orderLineItems);
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
