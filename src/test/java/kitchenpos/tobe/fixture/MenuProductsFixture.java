package kitchenpos.tobe.fixture;

import java.util.Arrays;
import kitchenpos.tobe.menus.domain.MenuProduct;
import kitchenpos.tobe.menus.domain.MenuProducts;

public class MenuProductsFixture {

    private MenuProductsFixture() {
    }

    public static MenuProducts of(final MenuProduct... menuProducts) {
        return new MenuProducts(Arrays.asList(menuProducts));
    }
}
