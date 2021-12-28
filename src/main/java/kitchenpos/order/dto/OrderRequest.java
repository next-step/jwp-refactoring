package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {
    Long orderTableId;
    String orderStatus;
    List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItemRequests;
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
