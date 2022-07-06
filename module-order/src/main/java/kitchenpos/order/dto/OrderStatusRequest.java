package kitchenpos.order.dto;

import static kitchenpos.common.message.ValidationMessage.NOT_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {
    @NotNull(message = NOT_NULL)
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
