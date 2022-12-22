package kitchenpos.menu.domain.fixture;

import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {

    public static MenuProduct menuProductA(Product product) {
        return new MenuProduct(product.getId(), new Quantity(1));
    }
}
