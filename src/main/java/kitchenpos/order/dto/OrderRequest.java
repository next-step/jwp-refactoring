package kitchenpos.order.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequest = new ArrayList<>();

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequest) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequest = orderLineItemRequestIsNull(orderLineItemRequest);
    }

    private List<OrderLineItemRequest> orderLineItemRequestIsNull(List<OrderLineItemRequest> orderLineItemRequest) {
        if (Objects.isNull(orderLineItemRequest)) {
            return new ArrayList<>();
        }

        return orderLineItemRequest;
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

    public boolean isRequestItemEmpty() {
        if (this.orderLineItemRequest.isEmpty()) {
            return true;
        }

        return false;
    }
}
