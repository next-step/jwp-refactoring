package kitchenpos.domain.order;

import kitchenpos.domain.Quantity;

public class OrderLineItemCreate {
    private long menuId;
    private Quantity quantity;

    public OrderLineItemCreate(long menuId, Quantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
