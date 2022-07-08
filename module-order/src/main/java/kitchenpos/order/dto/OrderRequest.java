package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    private OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toOrder() {
        return Order.of(orderTableId, retrieveOrderLineItems());
    }

    private List<OrderLineItem> retrieveOrderLineItems() {
        return orderLineItems.stream()
                .map(orderLineItemsRequest ->
                        OrderLineItem.of(orderLineItemsRequest.getMenuId(), orderLineItemsRequest.getQuantity()))
                .collect(Collectors.toList());
    }
}
