package kitchenpos.domain;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuantityTest {
    @DisplayName("수량은 0보다 작을 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -2L, -3L})
    void quantityUnderZeroException(Long value) {
        assertThatThrownBy(() -> new Quantity(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량은 0보다 작을 수 없습니다.");
    }
}
