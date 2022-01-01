package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}
