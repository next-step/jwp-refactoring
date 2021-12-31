package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct of(Long productId, long quantity) {
        return MenuProduct.of(productId, quantity);
    }
}
