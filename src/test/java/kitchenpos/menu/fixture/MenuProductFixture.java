package kitchenpos.menu.fixture;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {

    private MenuProductFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuProduct create(Long seq, Long productId, long quantity) {
        return MenuProduct.of(seq, null, productId, Quantity.of(quantity));
    }
}
