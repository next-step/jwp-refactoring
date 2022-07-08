package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderStatus;

public class OrderStatusRequest {
    private String orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }
}
