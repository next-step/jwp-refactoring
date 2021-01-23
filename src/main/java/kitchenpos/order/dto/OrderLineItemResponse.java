package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private long menuId;

    private long quantity;

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItemResponse(long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        Menu menu = orderLineItem.getMenu();
        return new OrderLineItemResponse(menu.getId(), orderLineItem.getQuantity());
    }

}
