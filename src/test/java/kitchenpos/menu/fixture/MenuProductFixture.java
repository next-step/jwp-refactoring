package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {
    private MenuProductFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuProduct create(Long productId, Long quantity) {
        return MenuProduct.of(productId, quantity);
    }
}
