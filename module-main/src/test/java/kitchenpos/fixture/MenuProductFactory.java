package kitchenpos.fixture;

import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;

public class MenuProductFactory {
    public static MenuProduct createMenuProduct(Long seq, Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct(seq, menu, product.getId(), Quantity.from(quantity));

        return menuProduct;
    }

    public static MenuProductRequest createMenuProductRequest(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

}
