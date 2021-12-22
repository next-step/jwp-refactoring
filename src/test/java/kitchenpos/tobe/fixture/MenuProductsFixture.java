package kitchenpos.tobe.fixture;

import java.util.Arrays;
import kitchenpos.tobe.menu.domain.MenuProduct;
import kitchenpos.tobe.menu.domain.MenuProducts;

public class MenuProductsFixture {

    private MenuProductsFixture() {
    }

    public static MenuProducts of(final MenuProduct... menuProducts) {
        return new MenuProducts(Arrays.asList(menuProducts));
    }
}
