package kitchenpos.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("갯수")
class QuantityTest {

    @DisplayName("갯수 생성한다.")
    @Test
    void create() {
        assertThatNoException().isThrownBy(() -> new Quantity(1));
    }

    @DisplayName("갯수가 음수일 수 없다.")
    @Test
    void create_fail_minimum() {
        assertThatThrownBy(() -> new Quantity(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
