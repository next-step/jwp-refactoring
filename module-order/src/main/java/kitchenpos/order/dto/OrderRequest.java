package kitchenpos.order.dto;

import java.util.List;
import java.util.Objects;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(getOrderTableId(), that.getOrderTableId()) && Objects.equals(getOrderLineItems(), that.getOrderLineItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderTableId(), getOrderLineItems());
    }
}
