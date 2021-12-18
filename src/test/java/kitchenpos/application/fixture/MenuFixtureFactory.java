package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;

public class MenuFixtureFactory {

    private MenuFixtureFactory() {}

    public static Menu create(long id, String name, long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, BigDecimal.valueOf(price), menuGroup.getId(), menuProducts);
    }
}
