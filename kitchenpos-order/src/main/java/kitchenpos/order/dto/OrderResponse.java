package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {

    private Long id;

    private Long orderTableId;

    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse() {}

    public OrderResponse(Order order, List<OrderLineItemResponse> orderLineItems) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus();
        this.orderedTime = order.getOrderedTime();
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order
            .getOrderLineItems()
            .getOrderLineItems()
            .stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());
        return new OrderResponse(order, orderLineItemResponses);
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
