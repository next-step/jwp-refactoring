package kitchenpos.utils.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixtureFactory {
    public static MenuProduct createMenuProduct(Product product, int quantity) {
        return MenuProduct.of(product, quantity);
    }
}
