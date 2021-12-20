package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(Order order, Menu menu) {
        return new OrderLineItem(order, menu, quantity);
    }
}
