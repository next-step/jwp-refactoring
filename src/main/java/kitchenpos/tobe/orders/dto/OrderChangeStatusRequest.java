package kitchenpos.tobe.orders.dto;

import kitchenpos.tobe.orders.domain.order.OrderStatus;

public class OrderChangeStatusRequest {

    private final OrderStatus status;

    public OrderChangeStatusRequest(final OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
