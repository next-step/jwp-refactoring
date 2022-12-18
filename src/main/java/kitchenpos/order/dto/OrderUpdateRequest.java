package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderUpdateRequest {

    private final OrderStatus orderStatus;

    protected OrderUpdateRequest() {
        this.orderStatus = null;
    }

    public OrderUpdateRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
