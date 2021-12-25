package kitchenpos.domain;

import kitchenpos.exception.InvalidQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest {
    @DisplayName("수량은 1보다 작으면 생성할 수 없다.")
    @Test
    void validate() {
        assertThatThrownBy(
                () -> Quantity.of(0L)
        ).isInstanceOf(InvalidQuantityException.class);
    }
}
