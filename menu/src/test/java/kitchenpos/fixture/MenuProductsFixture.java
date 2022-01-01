package kitchenpos.fixture;

import java.util.Arrays;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

public class MenuProductsFixture {

    private MenuProductsFixture() {
    }

    public static MenuProducts of(final MenuProduct... menuProducts) {
        return new MenuProducts(Arrays.asList(menuProducts));
    }
}
