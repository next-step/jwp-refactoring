package kitchenpos.menu.domain.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuFixtureFactory {
    private MenuFixtureFactory() {
    }

    public static Menu createMenu(MenuGroup menuGroup, String menuName, int menuPrice, List<MenuProduct> menuProducts) {
        return new Menu(menuName, new BigDecimal(menuPrice), menuGroup, menuProducts);
    }
}
