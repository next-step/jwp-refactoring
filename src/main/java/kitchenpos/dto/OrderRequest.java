package kitchenpos.dto;

import kitchenpos.domain.*;
import kitchenpos.domain.type.OrderStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.constants.ErrorCodeType.ORDER_LINE_ITEM_REQUEST;

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
