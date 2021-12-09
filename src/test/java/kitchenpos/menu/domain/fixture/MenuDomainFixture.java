package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menugroup.MenuGroup;

import java.math.BigDecimal;

public class MenuDomainFixture {

    public static Menu menu(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

}
