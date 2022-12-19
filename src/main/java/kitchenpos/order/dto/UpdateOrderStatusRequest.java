package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class UpdateOrderStatusRequest {
    private String orderStatus;

    protected UpdateOrderStatusRequest() {}

    private UpdateOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static UpdateOrderStatusRequest of(String orderStatus) {
        return new UpdateOrderStatusRequest(orderStatus);
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }
}
