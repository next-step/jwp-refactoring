package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItemResponses;

    private OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(), order.getOrderedTime());
    }

    public static OrderResponse of(Order order, List<OrderLineItemResponse> orderLineItemResponses) {
        OrderResponse orderResponse = OrderResponse.of(order);
        orderResponse.orderLineItemResponses = orderLineItemResponses;
        return orderResponse;
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

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }
}
