package kitchenpos.order.dto;

import kitchenpos.order.OrderStatus;

public class OrderStatusRequest {

    private String orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public OrderStatus toStatus() {
        return OrderStatus.valueOf(this.orderStatus);
    }
}
