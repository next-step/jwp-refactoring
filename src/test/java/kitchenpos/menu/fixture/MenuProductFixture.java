package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Quantity;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {

    private MenuProductFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuProduct create(Long seq, Product product, long quantity) {
        return MenuProduct.of(seq, null, product, Quantity.of(quantity));
    }
}
