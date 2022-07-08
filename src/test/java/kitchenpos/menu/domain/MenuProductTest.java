package kitchenpos.menu.domain;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("MenuProduct Domain test")
class MenuProductTest {

    @Test
    void validProductTest() {
        // given
        MenuProduct menuProduct = 상품이_등록안된_메뉴_상품_생성();

        // then
        assertThatThrownBy(() -> {
            menuProduct.validProduct();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    public static MenuProduct 상품이_등록안된_메뉴_상품_생성() {
        return MenuProduct.of(null, 1);
    }

    public static MenuProduct 상품이_등록된_메뉴_상품_생성() {
        return MenuProduct.of(1L, 2);
    }
}
