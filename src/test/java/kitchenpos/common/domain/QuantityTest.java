package kitchenpos.common.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest {
    @DisplayName("수량을 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = {0L, 1L, 2L})
    void createQuantity(Long value) {
        // when
        Quantity quantity = new Quantity(value);

        // then
        assertThat(quantity.value()).isEqualTo(value);
    }

    @DisplayName("수량은 0보다 작을 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -2L, -3L})
    void quantityUnderZeroException(Long value) {
        assertThatThrownBy(() -> new Quantity(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("수량은 0보다 작을 수 없습니다. input = %d", value));
    }
}
