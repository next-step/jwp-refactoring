package kitchenpos.__fixture__;

import kitchenpos.domain.MenuProduct;

public class MenuProductTestFixture {
    public static MenuProduct 메뉴_상품_1개_생성(final Long id) {
        return new MenuProduct(id, 1);
    }
}
