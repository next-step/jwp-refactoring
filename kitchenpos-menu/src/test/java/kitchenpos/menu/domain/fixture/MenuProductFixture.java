package kitchenpos.menu.domain.fixture;

import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct menuProductA(Long productId) {
        return new MenuProduct(productId, new Quantity(1));
    }
}
