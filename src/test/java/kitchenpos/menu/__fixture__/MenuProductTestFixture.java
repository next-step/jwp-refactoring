package kitchenpos.menu.__fixture__;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.request.MenuProductRequest;

public class MenuProductTestFixture {
    public static MenuProductRequest 메뉴_상품_요청_생성(final MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public static MenuProduct 메뉴_상품_1개_생성(final Product product) {
        return new MenuProduct(product, 1);
    }

    public static MenuProduct 메뉴_상품_1개_생성(final Product product, final Menu menu) {
        return new MenuProduct(product, menu, 1);
    }
}
