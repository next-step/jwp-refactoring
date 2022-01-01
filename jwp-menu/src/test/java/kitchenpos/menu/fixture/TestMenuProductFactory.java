package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class TestMenuProductFactory {

    public static MenuProduct 메뉴상품_생성됨(final Long id, final Menu menu, final Product product, final int quantity) {
        return MenuProduct.of(id, menu, product.getId(), quantity);
    }
}
