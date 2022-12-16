package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

public class MenuProductFixture {

    public static MenuProduct create(Menu menu, Product product, Long quantity) {
        return new MenuProduct(menu, product, quantity);
    }

}
