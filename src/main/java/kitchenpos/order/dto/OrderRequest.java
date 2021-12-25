package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    private long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    private OrderRequest(long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toOrder() {
        return Order.of(
                orderTableId
                , OrderStatus.COOKING
                , LocalDateTime.now()
                , OrderLineItems.of(orderLineItems.stream()
                        .map(OrderLineItemRequest::toOrderLineItem)
                        .collect(Collectors.toList())));
    }
}
