package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {
    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private Long orderTableId;
    private List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();

    protected OrderResponse() {
    }

    private OrderResponse(Long id, OrderStatus orderStatus, LocalDateTime orderedTime, Long orderTableId, List<OrderLineItemResponse> orderLineItemResponses) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTableId = orderTableId;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrderResponse of(Order order) {
        List<OrderLineItemResponse> collect = order.getOrderLineItems().toResponses();
        return new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime(),
                order.getOrderTableId(), collect);
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

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }
}
