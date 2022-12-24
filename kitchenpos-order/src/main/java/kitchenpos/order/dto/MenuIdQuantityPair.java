package kitchenpos.order.dto;

import kitchenpos.common.domain.Quantity;

public class MenuIdQuantityPair {
    private Long menuId;
    private Quantity quantity;

    public MenuIdQuantityPair() {
    }

    public MenuIdQuantityPair(Long menuId, Quantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
