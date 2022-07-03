package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItemRequests;

    private OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public OrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
