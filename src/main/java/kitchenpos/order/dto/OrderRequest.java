package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity(OrderRequest request) {
        return new Order(orderTableId, OrderLineItemRequest.toEntity(orderLineItems));
    }
}
