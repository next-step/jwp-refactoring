package kitchenpos.menu.domain;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductTestFixture {

    public static MenuProduct 메뉴_상품(Long productId, long quantity) {
        return MenuProduct.of(productId, quantity);
    }
}
