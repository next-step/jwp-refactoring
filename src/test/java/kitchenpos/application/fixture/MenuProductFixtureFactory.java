package kitchenpos.application.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixtureFactory {
    private MenuProductFixtureFactory() {
    }

    public static MenuProduct create(final Long seq, final Long menuId, final Long productId, final long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }
}
