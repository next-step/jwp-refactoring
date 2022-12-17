package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;

public class MenuProductTestFixture {

    public static MenuProduct generateMenuProduct(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product.getId(), quantity);
    }

    public static MenuProduct generateMenuProduct(Product product, long quantity) {
        return new MenuProduct(null, null, product.getId(), quantity);
    }

    public static MenuProductRequest generateMenuProductRequest(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
