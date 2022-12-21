package kitchenpos.order.dto;

import kitchenpos.order.domain.type.OrderStatus;

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
