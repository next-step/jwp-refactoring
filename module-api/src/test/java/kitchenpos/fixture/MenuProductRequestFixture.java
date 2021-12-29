package kitchenpos.fixture;

import kitchenpos.menu.dto.MenuProductRequest;

public class MenuProductRequestFixture {

    public static MenuProductRequest request생성(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
