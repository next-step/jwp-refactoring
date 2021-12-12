package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixtureFactory {

    private MenuProductFixtureFactory() {}

    public static MenuProduct create(long id, long menuId, long productId, long quantity) {
        return MenuProduct.of(id, Menu.from(menuId), Product.from(productId), quantity);
    }
}
