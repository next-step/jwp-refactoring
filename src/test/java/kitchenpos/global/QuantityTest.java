package kitchenpos.global;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.global.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {


    @DisplayName("수량은 1개 이상이어야 함")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void qtyIsBigZero(long value) {
        assertThatIllegalArgumentException().isThrownBy(
                () -> Quantity.from(value)
        );
    }

}
