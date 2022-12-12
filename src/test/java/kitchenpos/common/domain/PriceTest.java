package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @DisplayName("가격이 빈 값이면 에러가 발생한다.")
    @ParameterizedTest
    @NullSource
    void validatePriceNotEmpty(BigDecimal price) {
        // when & then
        assertThatThrownBy(() -> Price.from(price))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수가 0원보다 작으면 에러가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -10, -100 })
    void validatePriceLessThanZero(int price) {
        // when & then
        assertThatThrownBy(() -> Price.from(BigDecimal.valueOf(price)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}