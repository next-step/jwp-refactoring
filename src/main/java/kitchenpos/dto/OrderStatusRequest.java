package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.order.domain.OrderStatusV2;

public class OrderStatusRequest {
    private OrderStatusV2 orderStatus;

    public OrderStatusRequest() {
    }

    @JsonCreator
    public OrderStatusRequest(@JsonProperty OrderStatusV2 orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatusV2 getOrderStatus() {
        return orderStatus;
    }
}
