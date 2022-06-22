package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("수량 관련")
public class QuantityTest {

    @Test
    @DisplayName("수량이 0보다 작을 수 없다.")
    void createQuantity() {
        // when
        long quantity = -1L;
        // then
        assertThatThrownBy(() -> Quantity.of(quantity)).isInstanceOf(IllegalArgumentException.class);
    }
}
