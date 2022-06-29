package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderChangeStatusRequest {
    private final OrderStatus orderStatus;

    public OrderChangeStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
