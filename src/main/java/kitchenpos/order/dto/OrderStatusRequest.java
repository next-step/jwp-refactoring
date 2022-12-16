package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {
    private String orderStatus;

    private OrderStatusRequest() {
    }

    public OrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus.name();
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }
}
