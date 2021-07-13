package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {

    private OrderStatus orderStatus;

    public OrderStatusRequest() {
    }

    private OrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusRequest of(OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
