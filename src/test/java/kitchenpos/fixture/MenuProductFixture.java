package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 메뉴상품_데이터_생성(Long seq, Long productId, int quantity) {
        return new MenuProduct(seq, productId, quantity);
    }

}
