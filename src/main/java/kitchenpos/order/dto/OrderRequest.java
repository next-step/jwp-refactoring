package kitchenpos.order.dto;

import java.util.List;
import jdk.internal.net.http.common.Log;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    private OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests){
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
