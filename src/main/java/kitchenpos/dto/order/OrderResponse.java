package kitchenpos.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItemResponses;

    public OrderResponse() {

    }

    public OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItemResponse> orderLineItemResponses) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderTable().getId(),
            order.getOrderStatus(),
            order.getOrderedTime(),
            order.toOrderLineItemResponse()
        );
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }
}
