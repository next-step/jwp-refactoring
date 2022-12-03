package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusChangeRequest {

    private String status;

    public OrderStatusChangeRequest(OrderStatus status) {
        this.status = status.name();
    }

    public String getOrderStatus() {
        return this.status;
    }
}
