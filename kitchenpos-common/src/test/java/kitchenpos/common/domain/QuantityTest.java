package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {

    @DisplayName("수량이 0단위보다 작으면 에러가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = { -1, -10, -100 })
    void validateQuantityLessThanZero(long quantity) {
        // when & then
        assertThatThrownBy(() -> Quantity.from(quantity))
            .isInstanceOf(IllegalArgumentException.class);
    }
}