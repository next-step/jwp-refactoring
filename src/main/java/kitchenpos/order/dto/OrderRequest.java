package kitchenpos.order.dto;

import java.util.List;

import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItemRequests;
    
    private OrderRequest() {
    }

    private OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequests = orderLineItemRequests;
    }
    
    public static OrderRequest of(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItemRequests);
    }
    
    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest(orderTableId, null, orderLineItemRequests);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItemRequests;
    }

}
