package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Quantity;

import java.util.Arrays;
import java.util.List;

public class MenuProductsFixture {

    public static List<MenuProduct> menuProducts(Long productId) {
        return Arrays.asList(new MenuProduct(productId, new Quantity(1)), new MenuProduct(productId, new Quantity(1)));
    }
}
