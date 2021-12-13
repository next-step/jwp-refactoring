package kitchenpos.application.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {
    private MenuProductFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuProduct create(Product product, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }
}
