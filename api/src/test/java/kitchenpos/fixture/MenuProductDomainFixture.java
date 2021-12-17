package kitchenpos.fixture;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menuproduct.MenuProduct;
import kitchenpos.menu.domain.product.Product;

public class MenuProductDomainFixture {
    public static MenuProduct menuProduct(Menu menu, Product product, int quantity) {
        return new MenuProduct(menu, product, quantity);
    }
}
