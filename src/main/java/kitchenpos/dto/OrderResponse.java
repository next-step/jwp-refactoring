package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderResponse {
    private Long id;
    private OrderTableResponse orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public static OrderResponse of(Order order) {
        return new OrderResponse(
            order.getId(),
            OrderTableResponse.of(order.getOrderTable()),
            order.getOrderStatus(),
            order.getOrderedTime(),
            order.getOrderLineItems()
                .stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList()));
    }

    public OrderResponse() {
    }

    public OrderResponse(Long id, OrderTableResponse orderTable, OrderStatus orderStatus,
        LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTableResponse orderTable) {
        this.orderTable = orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItemResponse> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
