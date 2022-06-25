package kitchenpos.dto;

import kitchenpos.domain.OrderEntity;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    public OrderResponse(
            Long id,
            Long orderTableId,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(OrderEntity order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTable().getId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                order.getOrderLineItems()
                        .stream()
                        .map(orderLineItem -> OrderLineItemResponse.of(orderLineItem))
                        .collect(Collectors.toList())
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
