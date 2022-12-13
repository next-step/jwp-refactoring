package kitchenpos.application;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;

@DisplayName("메뉴 상품 테스트")
public class MenuProductTest {

    public static MenuProduct 메뉴_상품(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }
}
