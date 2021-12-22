package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.common.domain.Quantity;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct createMenuProduct(final Product product,
        final Quantity quantity) {
        return new MenuProduct(product, quantity);
    }
}
