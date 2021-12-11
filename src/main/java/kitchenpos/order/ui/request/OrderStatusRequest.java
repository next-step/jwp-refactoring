package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.order.domain.OrderStatus;

public final class OrderStatusRequest {

    private final String orderStatus;

    @JsonCreator
    public OrderStatusRequest(
        @JsonProperty("orderStatus") String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public OrderStatus status() {
        return OrderStatus.valueOf(orderStatus);
    }
}
