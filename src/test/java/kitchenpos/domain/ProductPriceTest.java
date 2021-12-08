package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * - 상품의 가격은 0 원 이상이어야 한다
 */
class ProductPriceTest {

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    @DisplayName("상품의 가격은 0 원 이상이어야 한다")
    void check(int 유효하지_않은_상품_가격) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ProductPrice(유효하지_않은_상품_가격));
    }
}