package kitchenpos.factory.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixtureFactory {
    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
