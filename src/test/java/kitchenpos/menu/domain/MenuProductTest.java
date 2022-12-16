package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {

    private Product product = Product.of(1L, "후라이드", BigDecimal.valueOf(16_000));

    @DisplayName("id가 같은 두 객체는 동등하다.")
    @Test
    void equalsTest() {
        MenuProduct menuProduct1 = MenuProduct.of(1L, product.getId(), 2);
        MenuProduct menuProduct2 = MenuProduct.of(1L, product.getId(), 2);

        Assertions.assertThat(menuProduct1).isEqualTo(menuProduct2);
    }

    @DisplayName("id가 다르면 두 객체는 동등하지 않다.")
    @Test
    void equalsTest2() {
        MenuProduct menuProduct1 = MenuProduct.of(1L, product.getId(), 2);
        MenuProduct menuProduct2 = MenuProduct.of(2L, product.getId(), 2);

        Assertions.assertThat(menuProduct1).isNotEqualTo(menuProduct2);
    }
}
