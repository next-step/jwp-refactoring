package kitchenpos.order.dto;

import kitchenpos.menu.Menu;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderMenu;

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
        return OrderLineItem.of(OrderMenu.of(menu.getId(), menu.getName(), menu.getPrice()), quantity);
    }
}
