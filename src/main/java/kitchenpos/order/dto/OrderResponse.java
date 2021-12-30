package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.order.domain.Order;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemResponse> orderLineItems;
    private LocalDateTime orderedTime;

    private OrderResponse(Long id, Long orderTableId, String orderStatus,
        List<OrderLineItemResponse> orderLineItems, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse from(Order order) {
        OrderLineItemResponses orderLineItemResponses = OrderLineItemResponses.from(order.getOrderLineItemValues());
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatusName(),
            orderLineItemResponses.getOrderLineItemResponses(), order.getOrderedTime());
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
