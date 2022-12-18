package kitchenpos.dto;

import kitchenpos.domain.Name;
import kitchenpos.domain.Quantity;

public class MenuQuantityPair {
    private Long menuId;
    private Name menuName;
    private Quantity quantity;

    public MenuQuantityPair(Name menuName, Quantity quantity) {
        this.menuName = menuName;
        this.quantity = quantity;
    }

    public MenuQuantityPair(Long menuId, Name menuName, Quantity quantity) {
        this(menuName, quantity);
        this.menuId = menuId;
    }

    public Name getMenuName() {
        return menuName;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
