package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PriceTest {
    @Test
    void 음수_예외() {
        assertThatThrownBy(
                () -> Price.from(-100)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}