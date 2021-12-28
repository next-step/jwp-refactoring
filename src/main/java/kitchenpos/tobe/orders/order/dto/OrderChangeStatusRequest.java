package kitchenpos.tobe.orders.order.dto;

import kitchenpos.tobe.orders.order.domain.OrderStatus;

public class OrderChangeStatusRequest {

    private final OrderStatus status;

    public OrderChangeStatusRequest(final OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
