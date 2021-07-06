package kitchenpos.dto.order;

import java.util.List;
import java.util.Objects;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderLineItemRequests, that.orderLineItemRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderLineItemRequests);
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "orderTableId=" + orderTableId +
                ", orderLineItemRequests=" + orderLineItemRequests +
                '}';
    }
}
