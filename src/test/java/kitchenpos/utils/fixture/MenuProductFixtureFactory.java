package kitchenpos.utils.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixtureFactory {
    public static MenuProduct createMenuProduct(Long productId, int quantity) {
        return MenuProduct.of(productId, quantity);
    }

    public static MenuProduct createMenuProduct(Long seq, Long productId, int quantity) {
        return MenuProduct.of(seq, productId, quantity);
    }
}
