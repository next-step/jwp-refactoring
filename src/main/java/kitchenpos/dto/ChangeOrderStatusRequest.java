package kitchenpos.dto;

import kitchenpos.domain.order.OrderStatus;

import javax.validation.constraints.NotNull;

public class ChangeOrderStatusRequest {
    @NotNull
    private OrderStatus orderStatus;

    protected ChangeOrderStatusRequest() {}

    public ChangeOrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
