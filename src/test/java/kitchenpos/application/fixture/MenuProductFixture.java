package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct createMenuProduct(final Product product,
        final Long quantity) {
        return new MenuProduct(product, quantity);
    }
}
