package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("방문한 손님 수 관련")
public class NumberOfGuestsTest {

    @Test
    @DisplayName("방문한 손님 수가 0보다 작을 수 없다.")
    void createQuantity() {
        // when
        long quantity = -1L;
        // then
        assertThatThrownBy(() -> Quantity.of(quantity)).isInstanceOf(IllegalArgumentException.class);
    }
}
