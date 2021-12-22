package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Collections;

public class MenuFixture {
    private MenuFixture() {
        throw new UnsupportedOperationException();
    }

    public static Menu create(Long id, String name, BigDecimal price, Long menuGroupId, MenuProduct menuProduct) {
        return Menu.of(id, name, price, menuGroupId, Collections.singletonList(menuProduct));
    }
}
