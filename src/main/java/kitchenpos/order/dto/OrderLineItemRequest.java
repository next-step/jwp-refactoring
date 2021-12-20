package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(Menu menu) {
        return OrderLineItem.of(menu, quantity);
    }
}
