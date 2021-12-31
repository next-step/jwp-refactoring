package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderChangeStatusRequest {

    private final OrderStatus status;

    public OrderChangeStatusRequest(final OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
