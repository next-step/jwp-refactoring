package kitchenpos.domain.menu.fixture;

import kitchenpos.domain.menu.domain.MenuProduct;
import kitchenpos.domain.product.domain.Product;
import kitchenpos.domain.menu.dto.MenuProductRequest;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Long productId, int quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProductRequest 메뉴_상품_요청(Long productId, int quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static MenuProductRequest 메뉴_상품_요청(Product product, int quantity) {
        return new MenuProductRequest(product.getId(), quantity);
    }
}
