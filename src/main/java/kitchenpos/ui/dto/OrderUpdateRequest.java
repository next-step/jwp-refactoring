package kitchenpos.ui.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderUpdateRequest {
    private String orderStatus;

    private OrderUpdateRequest() {
    }

    public OrderUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order toEntity() {
        return new Order(OrderStatus.valueOf(orderStatus));
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
