package kitchenpos.menu.fixture;

import kitchenpos.common.domain.fixture.NameFixture;
import kitchenpos.common.domain.fixture.PriceFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

import java.math.BigDecimal;
import java.util.Collections;

public class MenuFixture {
    private MenuFixture() {
        throw new UnsupportedOperationException();
    }

    public static Menu create(Long id, String name, BigDecimal price, MenuGroup menuGroup, MenuProduct menuProduct) {
        return Menu.of(id, NameFixture.of(name), PriceFixture.of(price), menuGroup, MenuProducts.of(Collections.singletonList(menuProduct)));
    }
}
