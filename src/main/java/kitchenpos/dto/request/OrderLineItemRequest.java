package kitchenpos.dto.request;

import kitchenpos.domain.orders.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return OrderLineItem.valueOf(menuId, quantity);
    }
}
