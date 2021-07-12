package kitchenpos.dto.order;

import java.util.List;
import java.util.Objects;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderLineItems);
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "orderTableId=" + orderTableId +
                ", orderLineItemRequests=" + orderLineItems +
                '}';
    }
}
