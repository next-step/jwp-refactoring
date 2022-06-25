package kitchenpos.domain;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductTest {

    public static MenuProduct 메뉴_상품_생성(Long menuId, Long productId, long quantity) {
        return new MenuProduct(menuId, productId, quantity);
    }
}
