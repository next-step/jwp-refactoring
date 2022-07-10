package kitchenpos.order.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderRequest {

    private long orderTableId;
    private String orderStatus;

    private List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();

    public OrderRequest() {

    }

    public OrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public OrderRequest(long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }
    public OrderRequest(long orderTableId, String orderStatus, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItemRequests;
    }
}
