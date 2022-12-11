package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

import static kitchenpos.fixture.ProductTestFixture.*;

public class MenuProductTestFixture {

    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        return MenuProduct.of(seq, menuId, productId, quantity);
    }

    public static MenuProduct 짜장면메뉴상품() {
        return createMenuProduct(1L, null, 짜장면_요청().getId(), 1L);
    }

    public static MenuProduct 짬뽕메뉴상품() {
        return createMenuProduct(2L, null, 짬뽕_요청().getId(), 1L);
    }

    public static MenuProduct 탕수육메뉴상품() {
        return createMenuProduct(3L, null, 탕수육_요청().getId(), 1L);
    }

    public static MenuProduct 단무지메뉴상품() {
        return createMenuProduct(4L, null, 단무지_요청().getId(), 1L);
    }
}
