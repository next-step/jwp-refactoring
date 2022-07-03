package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

import javax.validation.constraints.NotNull;

public class ChangeOrderStatusRequest {
    @NotNull(message = "변경할 주문 상태 값은 필수값입니다.")
    private OrderStatus orderStatus;

    public ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
