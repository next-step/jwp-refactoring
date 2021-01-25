package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.dto.OrderTableRequest;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public Order toOrder() {
        return Order.of(orderTableId, convertToOrderLineItems());
    }

    private List<OrderLineItem> convertToOrderLineItems() {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
    }
}
