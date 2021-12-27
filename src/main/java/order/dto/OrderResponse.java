package order.dto;

import java.time.*;
import java.util.*;

import order.domain.*;
import table.domain.*;

public class OrderResponse {

    private Long id;
    private OrderTable orderTable;
    private OrderStatus orderStatus;
    private List<OrderLineItem> orderLineItems;
    private LocalDateTime orderedTime;

    public OrderResponse() {
    }

    private OrderResponse(Order order) {
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderResponse that = (OrderResponse)o;
        return Objects.equals(orderTable, that.orderTable) && orderStatus == that.orderStatus
            && Objects.equals(orderLineItems, that.orderLineItems) && Objects.equals(orderedTime,
            that.orderedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTable, orderStatus, orderLineItems, orderedTime);
    }
}
