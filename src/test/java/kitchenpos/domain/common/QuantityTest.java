package kitchenpos.domain.common;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    @DisplayName("수량은 0 이상이어야 한다.")
    void validationTest() {
        assertThatThrownBy(
                () -> new Quantity(-1)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
