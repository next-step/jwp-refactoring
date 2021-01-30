package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {
    private long orderTableId;
    private List<OrderLineRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(long orderTableId, List<OrderLineRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineRequest> getOrderLineItems() {
        return orderLineItems;
    }
}