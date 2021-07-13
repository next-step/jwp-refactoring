package kitchenpos.order.dto;

import java.util.ArrayList;
import java.util.List;

import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {
    private OrderStatus orderStatus;
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();

    protected OrderRequest() {
    }

    public OrderRequest(OrderStatus orderStatus, Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderStatus = orderStatus;
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
