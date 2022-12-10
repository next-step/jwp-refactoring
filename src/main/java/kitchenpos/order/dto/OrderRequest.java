package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    private long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {}

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

    public Order createOrder(OrderTable orderTable) {
        List<OrderLineItem> orderLineItems = getOrderLineItems().stream()
                .map(OrderLineItemRequest::createOrderLineItem)
                .collect(Collectors.toList());
        return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }
}
