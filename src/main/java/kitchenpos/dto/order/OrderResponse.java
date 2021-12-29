package kitchenpos.dto.order;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.ordertable.OrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private OrderTableResponse orderTableResponse;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    private OrderResponse(final Long id, final OrderTableResponse orderTableResponse, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableResponse = orderTableResponse;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(final Long id, final OrderTableResponse orderTableResponse, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItemResponse> orderLineItems) {
        return new OrderResponse(id, orderTableResponse, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderResponse from(final Order order) {
        final OrderTableResponse orderTableResponse = OrderTableResponse.from(order.getOrderTable());
        final List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
                .toList()
                .stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), orderTableResponse, order.getOrderStatus(), order.getOrderedTime(), orderLineItemResponses);
    }

    public static List<OrderResponse> from(final List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
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
