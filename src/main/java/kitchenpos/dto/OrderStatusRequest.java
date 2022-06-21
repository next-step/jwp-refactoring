package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.OrderStatus;

public class OrderStatusRequest {
    private OrderStatus orderStatus;

    @JsonCreator
    public OrderStatusRequest(@JsonProperty("orderStatus") OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
