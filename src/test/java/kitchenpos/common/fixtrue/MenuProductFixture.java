package kitchenpos.common.fixtrue;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct of(Long productId, long quantity) {
        return MenuProduct.of(productId, quantity);
    }
}
