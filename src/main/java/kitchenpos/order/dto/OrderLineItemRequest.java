package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    private static Long setOrderLineItemOrderId(OrderLineItem orderLineItem) {
        Long orderId = null;
        Order order = orderLineItem.getOrder();
        if (order != null) {
            orderId = order.getId();
        }
        return orderId;
    }

    private static Long setOrderLineItemMenuId(OrderLineItem orderLineItem) {
        Long menuId = null;
        Menu menu = orderLineItem.getMenu();
        if (menu != null) {
            menuId = menu.getId();
        }
        return menuId;
    }

    public static OrderLineItemRequest of(OrderLineItem orderLineItem) {
        Long orderId = setOrderLineItemOrderId(orderLineItem);
        Long menuId = setOrderLineItemMenuId(orderLineItem);

        return new OrderLineItemRequest(orderLineItem.getSeq(), orderId, menuId, orderLineItem.getQuantity());
    }
}
