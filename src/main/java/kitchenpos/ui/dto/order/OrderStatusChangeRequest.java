package kitchenpos.ui.dto.order;

import java.util.Objects;

public class OrderStatusChangeRequest {
    private String orderStatus;

    protected OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderStatusChangeRequest that = (OrderStatusChangeRequest) o;
        return Objects.equals(orderStatus, that.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatus);
    }

    @Override
    public String toString() {
        return "OrderStatusChangeRequest{" +
                "orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
