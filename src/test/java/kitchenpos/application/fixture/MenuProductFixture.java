package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.ProductFixture.후리이드치킨;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuProductRequest;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProductRequest 요청_메뉴상품_치킨() {
        return new MenuProductRequest(1L, 1L);
    }

    public static List<MenuProduct> 메뉴상품_치킨_리스트() {
        return Collections.singletonList(MenuProduct.of(후리이드치킨(1L), 1L));
    }

    public static MenuProduct 메뉴상품(Product product) {
        return MenuProduct.of(product, 1L);
    }


}
