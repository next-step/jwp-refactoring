package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

public class MenuProductTestFixture {

    public static MenuProduct create(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public static MenuProduct create(Product product, long quantity) {
        return new MenuProduct(null, null, product, quantity);
    }
}
