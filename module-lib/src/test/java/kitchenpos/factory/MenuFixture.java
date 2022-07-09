package kitchenpos.factory;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class MenuFixture {
    public static MenuGroup 메뉴그룹_생성(Long id, String name) {
        return new MenuGroup(id, "name");
    }

    public static Product 상품생성(Long id, String name, int price) {
        return new Product(id, name, new BigDecimal(price));
    }

    public static MenuProduct 메뉴_상품_생성(Long productId) {
        return new MenuProduct(productId, 1);
    }
}
