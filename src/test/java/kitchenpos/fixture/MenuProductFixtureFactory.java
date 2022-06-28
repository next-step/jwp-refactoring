package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixtureFactory {
    private MenuProductFixtureFactory() {
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
        return new MenuProduct(productId,quantity);
    }
}
