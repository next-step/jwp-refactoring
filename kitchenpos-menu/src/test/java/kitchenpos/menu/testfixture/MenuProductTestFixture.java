package kitchenpos.menu.testfixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductTestFixture {

    public static MenuProduct create(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public static MenuProduct create(Product product, long quantity) {
        return new MenuProduct(null, null, product, quantity);
    }
}
