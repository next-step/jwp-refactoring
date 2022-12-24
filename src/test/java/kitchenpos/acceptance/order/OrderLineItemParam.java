package kitchenpos.acceptance.order;

import kitchenpos.acceptance.menu.MenuId;

public class OrderLineItemParam {
    private final long menuId;
    private final long quantity;

    public OrderLineItemParam(MenuId menuId, long quantity) {
        this.menuId = menuId.value();
        this.quantity = quantity;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
