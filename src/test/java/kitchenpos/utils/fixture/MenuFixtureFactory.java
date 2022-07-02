package kitchenpos.utils.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

import java.util.List;

public class MenuFixtureFactory {
    public static Menu createMenu(Long id, String name, int price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroup, menuProducts);
    }

    public static Menu createMenu(String name, int price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroup, menuProducts);
    }
}
