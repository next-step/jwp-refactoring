package kitchenpos.utils.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

import java.util.List;

public class MenuFixtureFactory {
    public static Menu createMenu(Long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu createMenu(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroupId, menuProducts);
    }
}
