package kitchenpos.dto;

import java.util.List;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
