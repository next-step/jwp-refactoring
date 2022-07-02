package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.order.domain.OrderStatus;

import java.util.Objects;

public class OrderStatusRequest {
    public OrderStatus orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(@JsonProperty("orderStatus") OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatusRequest that = (OrderStatusRequest) o;
        return getOrderStatus() == that.getOrderStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderStatus());
    }
}
