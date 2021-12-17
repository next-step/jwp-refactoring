package kitchenpos.menu.fixture;

import kitchenpos.common.domain.fixture.QuantityFixture;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {
    private MenuProductFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuProduct create(Product product, Long quantity) {
        return MenuProduct.of(product, QuantityFixture.create(quantity));
    }
}
