package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct createMenuProduct(final Long productId,
        final Long quantity) {
        return new MenuProduct(productId, quantity);
    }
}
