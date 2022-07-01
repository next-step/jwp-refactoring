package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {
    private OrderStatus status;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(OrderStatus status) {
        this.status = status;
    }

    public static OrderStatusRequest of(OrderStatus status) {
        return new OrderStatusRequest(status);
    }

    public OrderStatus getStatus() {
        return status;
    }
}