package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {
    private OrderStatus orderStatus;

    protected OrderStatusRequest() {
    }

    public OrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
