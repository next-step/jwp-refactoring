package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public OrderLineItem toEntity(Menu menu) {
        return new OrderLineItem(menu, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
