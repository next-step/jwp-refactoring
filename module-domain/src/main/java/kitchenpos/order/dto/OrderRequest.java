package kitchenpos.order.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    private List<OrderLineItem> toOrderLineItems() {
        return orderLineItems.stream()
                .map(it -> OrderLineItem.of(it.getMenuId(), Quantity.of(it.getQuantity())))
                .collect(Collectors.toList());
    }

    public Order toOrder() {
        return Order.of(orderTableId, toOrderLineItems());
    }
}
