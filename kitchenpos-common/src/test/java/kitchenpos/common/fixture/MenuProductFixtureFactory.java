package kitchenpos.common.fixture;

import kitchenpos.common.domain.menu.Menu;
import kitchenpos.common.domain.menu.MenuProduct;
import kitchenpos.common.domain.product.Product;

public class MenuProductFixtureFactory {

    private MenuProductFixtureFactory() {}

    public static MenuProduct create(long id, Menu menu, Product product, long quantity) {
        return MenuProduct.of(id, menu, product.getId(), quantity);
    }
}
