package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemRequest {

    private Long menuId;

    private long quantity;

    public OrderLineItemRequest() {}

    private OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest from(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public OrderLineItem toOrderLineItem(OrderMenu orderMenu) {
        return OrderLineItem.of(orderMenu, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
