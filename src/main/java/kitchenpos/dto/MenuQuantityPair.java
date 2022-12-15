package kitchenpos.dto;

import kitchenpos.domain.Menu;

public class MenuQuantityPair {
    private Menu menu;
    private Long quantity;

    public MenuQuantityPair(Menu menu, Long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
