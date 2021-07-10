package kitchenpos.domain;

import kitchenpos.common.exception.InvalidQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class QuantityTest {

    @Test
    @DisplayName("수량은 0이하가 불가능하다")
    void 수량은_0이하가_불가능하다() {
        assertThatExceptionOfType(InvalidQuantityException.class).isThrownBy(() -> new Quantity(0L));
        assertThatExceptionOfType(InvalidQuantityException.class).isThrownBy(() -> new Quantity(-1L));
    }
}