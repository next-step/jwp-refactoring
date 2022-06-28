package kitchenpos.ui.dto;

import kitchenpos.domain.Order;

public class OrderUpdateRequest {
    private String orderStatus;

    public Order toEntity() {
        return new Order(orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
