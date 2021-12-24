package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderRequest(final Long orderTableId, final String orderStatus,
        final List<OrderLineItemRequest> orderLineItem) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItem;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
