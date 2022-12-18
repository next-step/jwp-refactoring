package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductFixture.productA;

public class MenuProductFixture {

    public static MenuProduct menuProductA() {
        return new MenuProduct(null, productA(), 1L);
    }
}
