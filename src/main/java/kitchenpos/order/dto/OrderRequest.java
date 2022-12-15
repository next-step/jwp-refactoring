package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;

import java.util.ArrayList;
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

    public static OrderRequest of(Long orderTableId) {
        return new OrderRequest(orderTableId, OrderStatus.COOKING, new ArrayList<>());
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemRequest> collect = orderLineItems.stream().map(OrderLineItemRequest::of).collect(toList());
        return new OrderRequest(orderTableId, OrderStatus.COOKING, collect);
    }

    public Order toOrder(OrderTable orderTable, OrderStatus orderStatus, List<Menu> menus) {
        Order order = Order.of(orderTable, orderStatus);
        List<OrderLineItem> items = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.toOrderLineItem(order, menus))
                .collect(toList());
        order.addOrderLineItems(items);
        order.checkValidOrder(menus.size());
        return order;
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

    public List<Long> findAllMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(toList());
    }
}
