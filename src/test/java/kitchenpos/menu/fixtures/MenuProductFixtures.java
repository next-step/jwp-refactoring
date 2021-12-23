package kitchenpos.menu.fixtures;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
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
        return MenuProductRequest.of(2L, 2L);
    }

    public static MenuProductRequest 메뉴상품등록요청(Long productId, Long quantity) {
        return MenuProductRequest.of(productId, quantity);
    }

    public static MenuProduct 메뉴상품(Product product, Long quantity) {
        return new MenuProduct(product, quantity);
    }
}
