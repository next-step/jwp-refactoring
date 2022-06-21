package kitchenpos.domain;

import static kitchenpos.exception.NegativeQuantityException.INVALID_QUANTITY;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.exception.NegativeQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {

    @DisplayName("Quantity를 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = {0, 1, 100, 1000, 10000})
    void create01(long quantity) {
        assertThatNoException().isThrownBy(() -> Quantity.from(quantity));
    }

    @DisplayName("Quantity를 생성할 수 없다. (0보다 작은)")
    @ParameterizedTest
    @ValueSource(longs = {-1000, -10, -1})
    void create02(long quantity) {
        assertThatExceptionOfType(NegativeQuantityException.class)
                .isThrownBy(() -> Quantity.from(quantity))
                .withMessageContaining(String.format(INVALID_QUANTITY, quantity));
    }
}