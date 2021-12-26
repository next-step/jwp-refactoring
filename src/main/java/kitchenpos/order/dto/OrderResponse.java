package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(Order order, List<OrderLineItemResponse> orderLineItems) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus();
        this.orderedTime = order.getOrderedTime();
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        List<OrderLineItemResponse> orderLineItems = OrderLineItemResponse.listOf(order.getOrderLineItems());
        return new OrderResponse(order, orderLineItems);
    }

    public static List<OrderResponse> listOf(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
