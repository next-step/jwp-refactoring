package kitchenpos;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class TestMenuProductFactory {
    public static MenuProduct create(Long id, Long quantity, Product product) {
        return new MenuProduct(id, new Quantity(quantity), product);
    }
}
