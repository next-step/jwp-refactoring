package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.error.ErrorEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class QuantityTest {

    @ParameterizedTest
    @ValueSource(longs = {-1L, -2L})
    void 수량은_0보다_작을_수_없다(Long value) {
        assertThatThrownBy(() -> new Quantity(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.QUANTITY_UNDER_ZERO.message());
    }
}
