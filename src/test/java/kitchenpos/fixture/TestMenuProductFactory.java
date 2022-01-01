package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class TestMenuProductFactory {

    public static MenuProduct 메뉴상품_생성됨(final Long id, final Menu menu, final Product product, final int quantity) {
        return MenuProduct.of(id, menu, product.getId(), quantity);
    }
}
