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

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public Order toCookingOrder() {
        return Order.CookingOrder(orderTableId, getOrderLineItems());
    }
}
