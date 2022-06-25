package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {
    private OrderStatus orderStatus;

    public OrderStatusRequest() {
    }

    @JsonCreator
    public OrderStatusRequest(@JsonProperty OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
