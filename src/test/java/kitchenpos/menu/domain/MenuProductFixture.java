package kitchenpos.menu.domain;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;

public class MenuProductFixture {
    private MenuProductFixture() {
    }

    public static MenuProductRequest menuProductParam(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }



    public static MenuProduct savedMenuProduct(Long id, MenuProduct param) {
        return new MenuProduct(id, param.getProductId(), param.getQuantity());
    }

    public static MenuProduct savedMenuProduct(Long id, Long productId, long quantity) {
        return new MenuProduct(id, productId, quantity);
    }
}
