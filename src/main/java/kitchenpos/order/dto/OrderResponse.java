package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    private OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order savedOrder) {
        List<OrderLineItemResponse> orderLineItems = savedOrder.getOrderLineItems().stream()
            .map(orderLineItem -> OrderLineItemResponse.from(orderLineItem))
            .collect(Collectors.toList());
        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderTable().getId(),
            savedOrder.getOrderStatus(), orderLineItems);
    }

    public static List<OrderResponse> toOrderResponse(List<Order> orders) {
        return orders.stream()
            .map(order -> OrderResponse.from(order))
            .collect(Collectors.toList());
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
