package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {
    private String orderStatus;

    protected OrderStatusRequest() {
    }

    private OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusRequest of(OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus.name());
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }
}
