package kitchenpos.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("유효성 검사 테스트")
    @Test
    void validate() {
        // given
        Product product = new Product(1L, "상품", BigDecimal.valueOf(0));

        // when && then
        product.validate();
    }

    @DisplayName("유효성 검사 테스트 - price 값이 없거나 0 미만 일 경우 에러")
    @Test
    void validate_exception() {
        // given
        Product product1 = new Product(1L, "상품", null);

        // when && then
        assertThatThrownBy(product1::validate)
                .isInstanceOf(IllegalArgumentException.class);

        // given
        Product product2 = new Product(1L, "상품", BigDecimal.valueOf(-1));

        // when && then
        assertThatThrownBy(product2::validate)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
