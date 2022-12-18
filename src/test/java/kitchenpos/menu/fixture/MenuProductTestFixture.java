package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;

import static kitchenpos.product.fixture.ProductTestFixture.*;

public class MenuProductTestFixture {

    public static MenuProductRequest 메뉴상품요청(Menu menu, long productId, long quantity) {
        return MenuProductRequest.of(productId, quantity);
    }

    public static MenuProductRequest 짜장면메뉴상품요청() {
        return 메뉴상품요청(null, 1L, 1L);
    }

    public static MenuProductRequest 짬뽕메뉴상품요청() {
        return 메뉴상품요청(null, 2L, 1L);
    }

    public static MenuProductRequest 탕수육메뉴상품요청() {
        return 메뉴상품요청(null, 3L, 1L);
    }

    public static MenuProductRequest 단무지메뉴상품요청() {
        return 메뉴상품요청(null, 4L, 1L);
    }

    public static MenuProductRequest 짜장면메뉴상품요청(final Long 짜짱면상품ID) {
        return 메뉴상품요청(null, 짜짱면상품ID, 1L);
    }

    public static MenuProductRequest 짬뽕메뉴상품요청(final Long 짬뽕상품ID) {
        return 메뉴상품요청(null, 짬뽕상품ID, 1L);
    }

    public static MenuProductRequest 탕수육메뉴상품요청(final Long 탕수육상품ID) {
        return 메뉴상품요청(null, 탕수육상품ID, 1L);
    }

    public static MenuProductRequest 단무지메뉴상품요청(final Long 단무지상품ID) {
        return 메뉴상품요청(null, 단무지상품ID, 1L);
    }

    public static MenuProduct 짜장면메뉴상품() {
        return MenuProduct.of(상품생성(짜장면요청()), 1L);
    }

    public static MenuProduct 짬뽕메뉴상품() {
        return MenuProduct.of(상품생성(짬뽕요청()), 1L);
    }

}
