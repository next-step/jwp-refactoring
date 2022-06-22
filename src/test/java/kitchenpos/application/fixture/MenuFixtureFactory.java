package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;

public class MenuFixtureFactory {

    private MenuFixtureFactory() {}

    public static Menu create(String name, BigDecimal price, MenuGroup menuGroup) {
        return Menu.of(name, price, menuGroup);
    }
}
