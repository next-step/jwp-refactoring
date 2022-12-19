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

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<Long> findMenuIds() {
        return orderLineItems.stream().map(OrderLineItemRequest::getMenuId).collect(toList());
    }

    public Order toOrder(Long orderTableId, OrderStatus orderStatus) {
        Order order = Order.builder()
                .orderStatus(orderStatus)
                .orderTableId(orderTableId)
                .build();
        List<OrderLineItem> newOrderLineItems = orderLineItems.stream()
                .map(request -> request.toOrderLineItem(order))
                .collect(toList());
        order.addOrderLineItems(newOrderLineItems);
        return order;
    }
}
