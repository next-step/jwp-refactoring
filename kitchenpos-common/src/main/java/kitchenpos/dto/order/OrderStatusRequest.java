package kitchenpos.dto.order;


import kitchenpos.domain.order.OrderStatus;

public class OrderStatusRequest {
    private OrderStatus status;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
