package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Quantity;

public class MenuQuantityPair {
    private Menu menu;
    private Quantity quantity;

    public MenuQuantityPair(Menu menu, Quantity quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
