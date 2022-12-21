package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequest;

    protected OrderRequest() {}

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequest) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequest = orderLineItemRequest;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<Long> getMenuIds() {
        return orderLineItemRequest.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItemRequest> getOrderLineItemRequest() {
        return orderLineItemRequest;
    }

}
