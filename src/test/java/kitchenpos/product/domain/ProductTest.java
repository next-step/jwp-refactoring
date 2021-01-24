package kitchenpos.product.domain;

import kitchenpos.common.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @DisplayName("상품의 가격이 설정되어 있지 않을 경우 Exception 발생.")
    @Test
    void validatePriceException1() {
        assertThatThrownBy(() -> Product.of(1L, "후라이드", null))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격이 없거나 음수입니다.");
    }

    @DisplayName("상품의 가격이 음수일 경우 Exception 발생.")
    @Test
    void validatePriceException2() {
        assertThatThrownBy(() -> Product.of(1L, "후라이드", BigDecimal.valueOf(-1)))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격이 없거나 음수입니다.");
    }

    @DisplayName("상품의 가격에 수량을 곱한다.")
    @Test
    void multiplyQuantity() {
        Product product = Product.of(1L, "후라이드", BigDecimal.valueOf(16000));

        BigDecimal result = product.multiplyQuantity(BigDecimal.valueOf(3));

        assertThat(result).isEqualTo(BigDecimal.valueOf(48000));
    }
}
