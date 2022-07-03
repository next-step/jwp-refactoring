package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {


    @DisplayName("수량은 1개 이상이어야 함")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void qtyIsBigZero(long value) {
        assertThatIllegalArgumentException().isThrownBy(
                () -> Quantity.of(value)
        );
    }

}