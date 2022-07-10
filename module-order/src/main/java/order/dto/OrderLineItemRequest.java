package order.dto;

import menu.domain.Menu;
import order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private long quantity;

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    protected OrderLineItemRequest() {
    }

    public OrderLineItem toOrderLineItem(Menu menu) {
        return new OrderLineItem(menu.toOrderedMenu(), quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

}
