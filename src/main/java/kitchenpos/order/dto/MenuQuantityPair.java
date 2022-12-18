package kitchenpos.order.dto;

import kitchenpos.menu.domain.Quantity;
import kitchenpos.product.domain.Name;

public class MenuQuantityPair {
    private Name menuName;
    private Quantity quantity;

    public MenuQuantityPair(Name menuName, Quantity quantity) {
        this.menuName = menuName;
        this.quantity = quantity;
    }

    public Name getMenuName() {
        return menuName;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
