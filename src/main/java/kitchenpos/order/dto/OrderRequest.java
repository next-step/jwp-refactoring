package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public List<Long> findAllMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Order toOrder(Long orderTableId, List<Menu> menus) {
        Order order = new Order(orderTableId);
        List<OrderLineItem> items = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.toOrderLineItem(order, menus))
                .collect(toList());
        order.order(items);

        return order;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
