package order.dto;

import order.domain.Order;
import order.domain.OrderLineItem;
import order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItemsResponse;

    private OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                          List<OrderLineItemResponse> orderLineItemsResponse) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemsResponse = orderLineItemsResponse;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                order.getOrderedTime(), mapToOrderLineItemResponse(order.getOrderLineItems()));
    }

    private static List<OrderLineItemResponse> mapToOrderLineItemResponse(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public List<OrderLineItemResponse> getOrderLineItemsResponse() {
        return orderLineItemsResponse;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderResponse that = (OrderResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
