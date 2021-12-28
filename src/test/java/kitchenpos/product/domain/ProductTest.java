package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    @DisplayName("가격과 입력 값과의 곱을 반환합니다.")
    void multiplyPrice() {
        // given
        int priceValue = 16_000;
        Product product = Product.of("후라이드", new BigDecimal(priceValue));

        // when
        int inputValue = 2;
        BigDecimal result = product.multiplyPrice(new BigDecimal(inputValue));

        // then
        assertThat(result).isEqualTo(new BigDecimal(priceValue * inputValue));
    }
}