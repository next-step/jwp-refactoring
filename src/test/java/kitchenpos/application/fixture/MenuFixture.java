package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menu.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.Collections;

public class MenuFixture {
    private MenuFixture() {
        throw new UnsupportedOperationException();
    }

    public static Menu create(Long id, String name, BigDecimal price, MenuGroup menuGroup, MenuProduct menuProduct) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        return menu;
    }
}
