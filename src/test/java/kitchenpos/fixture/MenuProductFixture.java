package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuProductFixture {

    public static MenuProduct 메뉴상품_데이터_생성(Long seq, Long productId, int quantity) {
        return new MenuProduct(seq, productId, quantity);
    }

    public static void 메뉴상품_데이터_확인(MenuProduct menuProduct, Long seq, Long menuId, Long productId) {
        assertAll(
                () -> assertEquals(seq, menuProduct.getSeq()),
                () -> assertEquals(menuId, menuProduct.getMenuId()),
                () -> assertEquals(productId, menuProduct.getProductId())
        );
    }

}
