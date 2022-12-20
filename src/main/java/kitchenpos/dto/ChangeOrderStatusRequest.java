package kitchenpos.dto;

import kitchenpos.domain.type.OrderStatus;

public class ChangeOrderStatusRequest {
    private OrderStatus orderStatus;

    protected ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
