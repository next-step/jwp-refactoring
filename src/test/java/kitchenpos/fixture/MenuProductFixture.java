package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

public class MenuProductFixture {

    public static MenuProduct create(Product product, Long quantity) {
        return new MenuProduct(product, quantity);
    }

}
