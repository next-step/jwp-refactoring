package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private Long orderTableId;
    private List<OrderLineItemResponse> orderLineItems;

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime(),
                order.getOrderTableId(), OrderLineItemResponse.of(order.getOrderLineItems()));
    }

    public OrderResponse(Long id, OrderStatus orderStatus, LocalDateTime orderedTime,
                         Long orderTableId, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
