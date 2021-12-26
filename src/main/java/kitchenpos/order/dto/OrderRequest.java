package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderRequest() {
    }

    private OrderRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    private OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest from(OrderStatus orderStatus) {
        return new OrderRequest(orderStatus);
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
