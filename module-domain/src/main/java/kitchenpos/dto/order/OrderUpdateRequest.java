package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderStatus;

public class OrderUpdateRequest {
    private OrderStatus orderStatus;

    public OrderUpdateRequest() {
    }

    public OrderUpdateRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
