package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {
    private String orderStatus;

    protected OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus toOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
