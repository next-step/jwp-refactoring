package kitchenpos.application.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

public class MenuProductFixtureFactory {

    private MenuProductFixtureFactory() {}

    public static MenuProduct create(long id, long menuId, long productId, long quantity) {
        return MenuProduct.of(id, Menu.from(menuId), Product.from(productId), quantity);
    }
}
