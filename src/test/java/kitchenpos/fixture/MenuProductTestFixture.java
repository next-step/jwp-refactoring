package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductRequest;

import static kitchenpos.fixture.ProductTestFixture.*;

public class MenuProductTestFixture {

    public static MenuProductRequest createMenuProduct(Menu menu, long productId, long quantity) {
        return MenuProductRequest.of(productId, quantity);
    }

    public static MenuProductRequest 짜장면메뉴상품() {
        return createMenuProduct(null, 1L, 1L);
    }

    public static MenuProductRequest 짬뽕메뉴상품() {
        return createMenuProduct(null, 2L, 1L);
    }


    public static MenuProductRequest 탕수육메뉴상품() {
        return createMenuProduct(null, 3L, 1L);
    }

    public static MenuProductRequest 단무지메뉴상품() {
        return createMenuProduct(null, 4L, 1L);
    }

    public static MenuProductRequest 짜장면메뉴상품(final Long 짜짱면상품ID) {
        return createMenuProduct(null, 짜짱면상품ID, 1L);
    }

    public static MenuProductRequest 짬뽕메뉴상품(final Long 짬뽕상품ID) {
        return createMenuProduct(null, 짬뽕상품ID, 1L);
    }


    public static MenuProductRequest 탕수육메뉴상품(final Long 탕수육상품ID) {
        return createMenuProduct(null, 탕수육상품ID, 1L);
    }

    public static MenuProductRequest 단무지메뉴상품(final Long 단무지상품ID) {
        return createMenuProduct(null, 단무지상품ID, 1L);
    }

    public static MenuProduct 짜장면메뉴상품엔티티() {
        return MenuProduct.of(상품생성(짜장면_요청()), 1L);
    }

    public static MenuProduct 짬뽕메뉴상품엔티티() {
        return MenuProduct.of(상품생성(짬뽕_요청()), 1L);
    }

}
