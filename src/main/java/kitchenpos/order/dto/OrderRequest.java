package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public static OrderRequest of(Order order) {
        return new OrderRequest(order.getOrderTableId(),
                OrderLineItemRequest.ofList(order.getOrderLineItems().temporaryGetList()));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
