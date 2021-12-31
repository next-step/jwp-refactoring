package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("수량 테스트")
class QuantityTest {

    @DisplayName("수량이 null이면 예외가 발생한다.")
    @Test
    void createNull() {
        assertThatThrownBy(() -> Quantity.of(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최소 수량 보다 작으면 예외가 발생한다.")
    @Test
    void createLessThanMinimumQuantity() {
        assertThatThrownBy(() -> Quantity.of(Quantity.MINIMUM_QUANTITY - 1))
            .isInstanceOf(IllegalArgumentException.class);
    }
}