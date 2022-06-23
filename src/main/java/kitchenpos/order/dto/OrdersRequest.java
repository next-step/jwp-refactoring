package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrdersRequest {
    private long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    @JsonCreator
    public OrdersRequest(@JsonProperty("orderTableId") long orderTableId,
            @JsonProperty("orderLineItems") List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 반드시 존재해야 합니다.");
        }
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
