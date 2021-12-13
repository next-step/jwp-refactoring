package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public final class OrderRequest {

    private final long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(
        @JsonProperty("orderTableId") long orderTableId,
        @JsonProperty("orderLineItems") List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
