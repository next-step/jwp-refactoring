package kitchenpos.menu.event;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.util.List;

public class MenuDecideEvent {
    private final Menu menu;
    private final List<MenuProduct> menuProducts;

    public MenuDecideEvent(Menu menu, List<MenuProduct> menuProducts) {
        this.menu = menu;
        this.menuProducts = menuProducts;
    }

    public Menu getMenu() {
        return menu;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
