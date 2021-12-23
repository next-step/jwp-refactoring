package kitchenpos.domain.dto;

import kitchenpos.domain.OrderStatus;

public class OrderStatusRequest {

    private String orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }
}
