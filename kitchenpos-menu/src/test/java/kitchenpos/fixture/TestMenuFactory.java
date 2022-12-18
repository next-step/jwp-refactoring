package kitchenpos.fixture;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public class TestMenuFactory {
    public static Menu create(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(1L, new Name(name), new Price(price), menuGroupId, new MenuProducts(menuProducts));
    }
}
