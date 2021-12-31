package kitchenpos.application.order.dto;

import kitchenpos.core.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final List<OrderLineItemResponse> orderLineItems;
    private final LocalDateTime orderedTime;

    public OrderResponse(Long id, Long orderTableId, String orderStatus, List<OrderLineItemResponse> orderLineItems, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse of(Order order) {
        final List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems().stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(), orderLineItems, order.getOrderedTime());
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

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;

        OrderResponse that = (OrderResponse) target;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(orderTableId, that.orderTableId)) return false;
        if (!Objects.equals(orderStatus, that.orderStatus)) return false;
        if (!Objects.equals(orderLineItems, that.orderLineItems))
            return false;
        return Objects.equals(orderedTime, that.orderedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderLineItems, orderedTime);
    }
}
