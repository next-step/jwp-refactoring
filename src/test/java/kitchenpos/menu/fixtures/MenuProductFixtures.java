package kitchenpos.menu.fixtures;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;

/**
 * packageName : kitchenpos.fixtures
 * fileName : MenuProductFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class MenuProductFixtures {
    public static MenuProductRequest 메뉴상품_두개요청() {
        return MenuProductRequest.of(1L, 2L);
    }

    public static MenuProductRequest 메뉴상품등록요청(Long productId, Long quantity) {
        return MenuProductRequest.of(productId, quantity);
    }

    public static MenuProduct 메뉴상품(Long productId, Long quantity) {
        return 메뉴상품등록요청(productId, quantity).toEntity();
    }

}
