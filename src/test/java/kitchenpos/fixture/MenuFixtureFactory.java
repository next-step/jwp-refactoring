package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuFixtureFactory {
    private MenuFixtureFactory() {
    }

    public static Menu createMenu(MenuGroup menuGroup, String menuName, int menuPrice, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName(menuName);
        menu.setPrice(new BigDecimal(menuPrice));
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
