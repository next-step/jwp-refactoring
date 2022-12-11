package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import static kitchenpos.fixture.ProductTestFixture.*;

public class MenuProductTestFixture {

    public static MenuProduct createMenuProduct(Long seq, Menu menu, Product product, long quantity) {
        return MenuProduct.of(seq, menu, product, quantity);
    }

    public static MenuProduct 짜장면메뉴상품() {
        return createMenuProduct(1L, null, 상품생성(짜장면_요청()), 1L);
    }

    public static MenuProduct 짬뽕메뉴상품() {
        return createMenuProduct(2L, null, 상품생성(짬뽕_요청()), 1L);
    }

    public static MenuProduct 탕수육메뉴상품() {
        return createMenuProduct(3L, null, 상품생성(탕수육_요청()), 1L);
    }

    public static MenuProduct 단무지메뉴상품() {
        return createMenuProduct(4L, null, 상품생성(단무지_요청()), 1L);
    }
}
