package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;

public class MenuProductFixture {
    private MenuProductFixture() {
    }

    public static MenuProductRequest menuProductRequest(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static MenuProduct savedMenuProduct(Long id, Long productId, long quantity) {
        return new MenuProduct(id, productId, quantity);
    }

    public static MenuProduct savedMenuProduct(Long id) {
        return new MenuProduct(id, 1L, 1L);
    }
}
