package kitchenpos.application.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixtureFactory {

    private MenuProductFixtureFactory() {}

    public static MenuProduct create(long id, long menuId, long productId, long quantity) {
        return MenuProduct.of(id, Menu.from(menuId), Product.from(productId), quantity);
    }
}
