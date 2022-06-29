package kitchenpos.factory;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class MenuFixture {
    public static MenuGroup 메뉴그룹_생성(Long id, String name) {
        return new MenuGroup(id, "name");
    }

    public static Product 상품생성(Long id, String name, int price) {
        return new Product(id, name, new BigDecimal(price));
    }

    public static MenuProduct 메뉴_상품_생성(Product product) {
        return new MenuProduct(product, 1);
    }
}
