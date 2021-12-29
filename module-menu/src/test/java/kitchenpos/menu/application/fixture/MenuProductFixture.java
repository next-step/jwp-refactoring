package kitchenpos.menu.application.fixture;

import static kitchenpos.product.application.fixture.ProductFixture.후리이드치킨;

import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProductRequest 요청_메뉴상품_치킨() {
        return new MenuProductRequest(null, 1L);
    }

    public static List<MenuProduct> 메뉴상품_치킨_리스트() {
        return Collections.singletonList(MenuProduct.of(후리이드치킨().getId(), 1L));
    }

    public static MenuProduct 메뉴상품(Product product) {
        return MenuProduct.of(product.getId(), 1L);
    }


}
