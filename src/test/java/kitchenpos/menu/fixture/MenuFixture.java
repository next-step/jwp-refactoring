package kitchenpos.menu.fixture;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    private MenuFixture() {
        throw new UnsupportedOperationException();
    }

    public static Menu create(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(id, Name.of(name), Price.of(price), menuGroupId, MenuProducts.of(menuProducts));
    }
}
