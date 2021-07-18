package kitchenpos.order.dto;

import java.time.LocalDateTime;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {

    private final Long id;

    private final Long orderTableId;

    private final String orderStatus;

    private final LocalDateTime orderedTime;

    public OrderResponse(final Long id, final Long orderTableId, final String orderStatus,
        final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse of(final Order order) {
        final OrderStatus orderStatus = order.getOrderStatus();
        return new OrderResponse(order.getId(), order.getOrderTableId(), orderStatus.name(),
            order.getOrderedTime());
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

}
