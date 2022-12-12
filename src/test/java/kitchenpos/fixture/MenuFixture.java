package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static Menu create(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

}
