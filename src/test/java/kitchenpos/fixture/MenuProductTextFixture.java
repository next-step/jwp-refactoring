package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductTextFixture {
    public static MenuProduct 생성(Long seq, Long productId, long quantity) {
        return new MenuProduct(seq, productId, quantity);
    }
}
