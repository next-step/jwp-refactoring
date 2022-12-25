package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemRequest> list(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getMenuId(),
                orderLineItem.getQuantity()))
            .collect(toList());
    }

    public OrderLineItem toOrderLineItem(Order order, List<Long> menus) {
        Long firstMenuId = menus.stream().filter(aLong -> aLong.equals(menuId)).findFirst().get();
        return new OrderLineItem(order, firstMenuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
