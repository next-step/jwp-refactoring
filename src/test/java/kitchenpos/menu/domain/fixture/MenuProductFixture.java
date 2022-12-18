package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.MenuProduct;

import static kitchenpos.product.domain.fixture.ProductFixture.productA;

public class MenuProductFixture {

    public static MenuProduct menuProductA() {
        return new MenuProduct(null, productA(), 1L);
    }
}
