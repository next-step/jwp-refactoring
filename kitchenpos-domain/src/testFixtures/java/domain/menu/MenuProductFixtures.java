package domain.menu;

import kitchenpos.menu.domain.MenuProduct;


public class MenuProductFixtures {
    public static MenuProduct 메뉴상품(Long productId, Long quantity) {
        return new MenuProduct(productId, quantity);
    }
}
