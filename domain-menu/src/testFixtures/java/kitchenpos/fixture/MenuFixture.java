package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProducts;

import java.math.BigDecimal;

public class MenuFixture {

    private MenuFixture() {

    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return Menu.of(name, price, menuGroup, menuProducts);
    }
}
