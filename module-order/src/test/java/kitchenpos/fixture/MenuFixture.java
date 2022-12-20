package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

import java.math.BigDecimal;

public class MenuFixture {

    public static Menu create(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

}
