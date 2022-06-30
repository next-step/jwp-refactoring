package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    Long id;
    Long orderTableId;
    String orderStatus;
    List<OrderLineItemResponse> orderLineItems;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long orderTableId, String orderStatus, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        List<OrderLineItemResponse> responses = order.getOrderLineItems().stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTable().getId(), order.getOrderStatus().name(), responses);
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
}
