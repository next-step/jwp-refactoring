package kitchenpos.domain.order;

import kitchenpos.domain.common.Quantity;
import kitchenpos.domain.menu.Menu;

public class MenuQuantity {
    private final Menu menu;
    private final Quantity quantity;

    public MenuQuantity(Menu menu, Quantity quantity) {
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
