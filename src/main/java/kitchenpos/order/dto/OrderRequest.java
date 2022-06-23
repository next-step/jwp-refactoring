package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }

    public Order toEntity() {
        return Order.of(orderTableId, orderLineItemRequests.stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList()));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}