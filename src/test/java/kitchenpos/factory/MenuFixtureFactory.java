package kitchenpos.factory;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menuGroup.domain.MenuGroup;

import java.math.BigDecimal;

public class MenuFixtureFactory {
    public static Menu createMenu(Long id, String name, int price, MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup);
    }

    public static Menu createMenu(String name, int price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }
}
