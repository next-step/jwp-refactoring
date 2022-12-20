package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuProductTest.메뉴상품_생성;
import static kitchenpos.product.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    @DisplayName("정상 생성")
    void create() {
        // given
        Product 미역국 = 상품_생성(1L, "미역국", BigDecimal.valueOf(6000));
        MenuProduct 미역국_메뉴상품 = 메뉴상품_생성(1L, 미역국, 1L);

        // when
        MenuProducts 메뉴상품들 = MenuProducts.from(Arrays.asList(미역국_메뉴상품));

        // then
        assertThat(메뉴상품들).isNotNull();
    }
}