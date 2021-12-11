package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;

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

    public Order toEntity() {
        return Order.of(orderTableId,
            OrderLineItems.from(orderLineItems.stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList())));
    }
}
