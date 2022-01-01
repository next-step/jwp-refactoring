package kitchenpos.order.dto;

import kitchenpos.domain.Quantity;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private final Long menuId;

    private final long quantity;

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(
            getMenuId(),
            new Quantity(getQuantity())
        );
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
