package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("수량")
class QuantityTest {
    @DisplayName("수량이 0이하이면 생성할 수 없습니다.")
    @Test
    void 수량이_0이하L() {
        assertThatThrownBy(() -> Quantity.from(-1L)).isInstanceOf(IllegalArgumentException.class);
    }
}
