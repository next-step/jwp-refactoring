package kitchenpos.menu.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import product.domain.Product;

import java.math.BigDecimal;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {

    private Product product = Product.of("허니콤보", BigDecimal.valueOf(18000));

    @DisplayName("id가 같은 두 객체는 같다.")
    @Test
    void equalsTest() {
        MenuProduct menuProduct1 = MenuProduct.of(1L, product.getId(), 2);
        MenuProduct menuProduct2 = MenuProduct.of(1L, product.getId(), 2);

        Assertions.assertThat(menuProduct1).isEqualTo(menuProduct2);
    }

    @DisplayName("id가 다르면 두 객체는 다르다.")
    @Test
    void equalsTest2() {
        MenuProduct menuProduct1 = MenuProduct.of(1L, product.getId(), 2);
        MenuProduct menuProduct2 = MenuProduct.of(2L, product.getId(), 2);

        Assertions.assertThat(menuProduct1).isNotEqualTo(menuProduct2);
    }
}
