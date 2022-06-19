package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

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
        assertThatIllegalArgumentException().isThrownBy(() -> Quantity.from(quantity))
                .withMessageContaining(String.format(Quantity.INVALID_QUANTITY, quantity));
    }
}