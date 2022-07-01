package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("유효성 검사 테스트")
    @Test
    void validate() {
        // when && then
        new Product(1L, "상품", BigDecimal.valueOf(0));
    }

    @DisplayName("유효성 검사 테스트 - price 값이 없거나 0 미만 일 경우 에러")
    @Test
    void validate_exception() {
        // when && then
        assertThatThrownBy(() -> new Product(1L, "상품", null))
                .isInstanceOf(IllegalArgumentException.class);

        // when && then
        assertThatThrownBy(() -> new Product(1L, "상품", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
