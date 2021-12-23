package kitchenpos.order.domain.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {

    private String orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusRequest cooking() {
        return new OrderStatusRequest("COOKING");
    }

    public static OrderStatusRequest meal() {
        return new OrderStatusRequest("MEAL");
    }

    public static OrderStatusRequest completion() {
        return new OrderStatusRequest("COMPLETION");
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }
}
