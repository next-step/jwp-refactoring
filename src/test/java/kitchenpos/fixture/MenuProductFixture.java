package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Product product, int quantity) {
        return new MenuProduct(product, quantity);
    }

    public static MenuProductRequest 메뉴_상품_요청(Long productId, int quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static MenuProductRequest 메뉴_상품_요청(Product product, int quantity) {
        return new MenuProductRequest(product.getId(), quantity);
    }
}
