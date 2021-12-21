package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderStatus;

public class OrderStatusRequest {

    private OrderStatus orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
