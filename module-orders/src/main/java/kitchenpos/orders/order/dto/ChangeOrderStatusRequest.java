package kitchenpos.orders.order.dto;

import java.util.Objects;
import kitchenpos.orders.order.domain.OrderStatus;

public class ChangeOrderStatusRequest {

    private Long orderTableId;
    private String orderStatus;

    public ChangeOrderStatusRequest() {
    }

    protected ChangeOrderStatusRequest(Long orderTableId, String orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public static ChangeOrderStatusRequest of(Long orderTableId, String orderStatus) {
        return new ChangeOrderStatusRequest(orderTableId, orderStatus);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChangeOrderStatusRequest that = (ChangeOrderStatusRequest) o;
        return Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderStatus,
                that.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderStatus);
    }
}
