package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.product.domain.Product;

public class TestMenuProductFactory {
    public static MenuProduct create(Menu menu, Product product, long quantity) {
        return create(null, menu, product, quantity);
    }

    public static MenuProduct create(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product.getId(), new Quantity(quantity));
    }
}
