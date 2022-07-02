package kitchenpos.service.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrdersRequest {
    private long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    @JsonCreator
    public OrdersRequest(@JsonProperty("orderTableId") long orderTableId,
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
