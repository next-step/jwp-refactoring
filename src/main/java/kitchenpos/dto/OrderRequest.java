package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
