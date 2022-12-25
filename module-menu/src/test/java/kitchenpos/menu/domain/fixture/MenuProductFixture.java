package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Quantity;

public class MenuProductFixture {

    public static MenuProduct menuProductA(Long productId) {
        return new MenuProduct(productId, new Quantity(1));
    }
}
