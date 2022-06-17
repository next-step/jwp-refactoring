package kitchenpos.application.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixtureFactory {

    private MenuProductFixtureFactory() {}

    public static MenuProduct create(Long menuId, Long productId, long quantity) {
        return MenuProduct.of(menuId, productId, quantity);
    }

    public static MenuProduct create(Long id, Long menuId, Long productId, long quantity) {
        return MenuProduct.of(id, menuId, productId, quantity);
    }
}
