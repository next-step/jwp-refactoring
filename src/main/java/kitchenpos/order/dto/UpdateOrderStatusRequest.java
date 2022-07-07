package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.domain.OrderStatus;

import java.util.Objects;

public class UpdateOrderStatusRequest {
    private final OrderStatus orderStatus;

    @JsonCreator
    public UpdateOrderStatusRequest(@JsonProperty("orderStatus") final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String toString() {
        return "UpdateOrderStatusRequest{" +
                "orderStatus='" + orderStatus + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UpdateOrderStatusRequest that = (UpdateOrderStatusRequest) o;
        return Objects.equals(orderStatus, that.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatus);
    }
}
