package kitchenpos.utils.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixtureFactory {
    public static MenuProduct createMenuProduct(Menu menu, Product product, int quantity) {
        return new MenuProduct(menu, product, quantity);
    }
}
