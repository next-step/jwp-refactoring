package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

import java.util.Collections;
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

    public Order toOrder(OrderTable orderTable, OrderStatus orderStatus, List<Menu> menus) {
        Order order = Order.builder()
                .orderStatus(orderStatus)
                .orderTable(orderTable)
                .build();
        List<OrderLineItem> newOrderLineItems = orderLineItems.stream()
                .filter(request -> menus.stream().anyMatch(menu -> menu.getId().equals(request.getMenuId())))
                .map(orderLineItem -> orderLineItem.toOrderLineItem(order, menus))
                .collect(toList());
        order.addOrderLineItems(newOrderLineItems);
        return order;
    }
}
