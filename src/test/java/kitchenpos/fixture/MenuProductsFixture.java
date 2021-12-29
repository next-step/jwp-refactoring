package kitchenpos.fixture;

import java.util.Arrays;
import kitchenpos.menus.menu.domain.MenuProduct;
import kitchenpos.menus.menu.domain.MenuProducts;

public class MenuProductsFixture {

    private MenuProductsFixture() {
    }

    public static MenuProducts of(final MenuProduct... menuProducts) {
        return new MenuProducts(Arrays.asList(menuProducts));
    }
}
