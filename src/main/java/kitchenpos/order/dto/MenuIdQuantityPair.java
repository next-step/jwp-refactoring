package kitchenpos.order.dto;

import kitchenpos.order.domain.ItemQuantity;

public class MenuIdQuantityPair {
    private Long menuId;
    private ItemQuantity quantity;

    public MenuIdQuantityPair() {
    }

    public MenuIdQuantityPair(Long menuId, ItemQuantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public ItemQuantity getQuantity() {
        return quantity;
    }
}
