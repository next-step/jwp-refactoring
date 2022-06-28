package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.Quantity;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;

public class MenuProductFactory {
    public static MenuProduct createMenuProduct(Long seq, Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct(seq, menu, product, Quantity.from(quantity));

        return menuProduct;
    }

    public static MenuProductRequest createMenuProductRequest(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

}
