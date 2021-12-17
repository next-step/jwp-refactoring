package kitchenpos.order.dto;

import kitchenpos.order.domain.order.Order;
import kitchenpos.order.domain.order.OrderStatus;
import kitchenpos.order.domain.orderLineItem.OrderLineItem;
import kitchenpos.table.domain.table.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

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

    public static OrderResponse of(Order order) {
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
}
