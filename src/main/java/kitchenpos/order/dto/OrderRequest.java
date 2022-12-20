package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {}

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(Long orderTableId, OrderStatus orderStatus) {
        Order order = new Order(orderTableId, orderStatus);
        List<OrderLineItem> items = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.toOrderLineItem(order))
                .collect(toList());
        order.order(items);

        return order;
    }

    public List<Long> toMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(toList());
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
}
