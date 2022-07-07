package kitchenpos.common;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidQuantityException;
import org.junit.jupiter.api.Test;

class QuantityTest {
    @Test
    void 한개_이하_예외() {
        assertThatThrownBy(
                () -> Quantity.from(0)
        ).isInstanceOf(InvalidQuantityException.class);
    }
}
