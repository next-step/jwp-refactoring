package kitchenpos.domain;

import static kitchenpos.domain.ProductTestFixture.*;

public class MenuProductTestFixture {

    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        return MenuProduct.of(seq, menuId, productId, quantity);
    }

    public static MenuProduct 짜장면상품() {
        return createMenuProduct(1L, null, 짜장면().getId(), 1L);
    }

    public static MenuProduct 짬뽕상품() {
        return createMenuProduct(2L, null, 짬뽕().getId(), 1L);
    }

    public static MenuProduct 탕수육상품() {
        return createMenuProduct(3L, null, 탕수육().getId(), 1L);
    }

    public static MenuProduct 단무지상품() {
        return createMenuProduct(4L, null, 단무지().getId(), 1L);
    }
}
