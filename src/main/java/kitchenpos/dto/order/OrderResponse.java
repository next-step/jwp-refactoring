package kitchenpos.dto.order;

import kitchenpos.domain.Order;
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

    private OrderResponse(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItemResponse> orderLineItems) {
        return new OrderResponse(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderResponse from(final Order order) {
        final List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
                .toList()
                .stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), orderLineItemResponses);
    }

    public static List<OrderResponse> from(final List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::from)
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
