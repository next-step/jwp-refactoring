package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequestDto;

public class MenuProductFixture {

    public static MenuProductRequestDto 메뉴상품_요청_데이터_생성(Long productId, long quantity) {
        return new MenuProductRequestDto(productId, quantity);
    }

    public static MenuProduct 메뉴상품_데이터_생성(Long seq, Product product, int quantity) {
        return new MenuProduct(seq, product, quantity);
    }

}
