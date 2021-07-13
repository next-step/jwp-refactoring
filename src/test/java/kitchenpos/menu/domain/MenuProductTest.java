package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Product;

@DisplayName("MenuProduct 클래스 기능 테스트")
class MenuProductTest {

    @Test
    @DisplayName("제품 가격과 개수의 곱셈 계산")
    void multiplyProductPriceByQuantity() {
        // given
        MenuProduct menuProduct = new MenuProduct(null, new Product("a", BigDecimal.valueOf(13000.00)), 4);

        // when
        BigDecimal totalPrice = menuProduct.multiplyProductPriceByQuantity();

        // then
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(52000.00));
    }
}
