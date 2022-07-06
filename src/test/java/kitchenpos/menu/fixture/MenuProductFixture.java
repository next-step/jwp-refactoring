package kitchenpos.menu.fixture;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;

public class MenuProductFixture {
    public static MenuProduct 메뉴_치킨 = create(1L, ProductFixture.치킨, Quantity.of(1L));
    public static MenuProduct 메뉴_양념_치킨 = create(2L, ProductFixture.양념_치킨, Quantity.of(1L));

    public static MenuProduct create(Long seq, Product product, Quantity quantity) {
        return MenuProduct.of(seq, null, product.getId(), quantity);
    }
}
