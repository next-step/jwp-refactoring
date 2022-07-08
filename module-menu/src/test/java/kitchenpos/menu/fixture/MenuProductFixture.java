package kitchenpos.menu.fixture;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {
    public static MenuProduct 메뉴_치킨 = create(1L, 1L, Quantity.of(1L));
    public static MenuProduct 메뉴_양념_치킨 = create(2L, 1L, Quantity.of(1L));

    public static MenuProduct create(Long seq, Long productID, Quantity quantity) {
        return MenuProduct.of(seq, null, productID, quantity);
    }
}
