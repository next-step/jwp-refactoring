package kitchenpos.utils.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuFixtureFactory {
    public static Menu createMenu(String name, int price, MenuGroup menuGroup) {
        return Menu.of(name, price, menuGroup);
    }
}
