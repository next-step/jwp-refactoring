package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequest;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequest) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequest = orderLineItemRequest;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequest() {
        return orderLineItemRequest;
    }

    public List<Long> getMenuId() {
        return orderLineItemRequest.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}
