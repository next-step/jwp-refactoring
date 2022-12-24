package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;

public class MenuProductFixture {
    private MenuProductFixture() {
    }

    public static MenuProductRequest menuProductRequest(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static MenuProduct savedMenuProduct(Long id, Long productId, long quantity) {
        return MenuProduct.of(id, productId, quantity);
    }
}
