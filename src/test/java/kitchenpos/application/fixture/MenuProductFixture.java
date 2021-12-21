package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.Quantity;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct createMenuProduct(final Product product,
        final Quantity quantity) {
        return new MenuProduct(product, quantity);
    }
}
