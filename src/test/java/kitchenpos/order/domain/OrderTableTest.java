package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @DisplayName("초기화 테스트")
    @Test
    void of() {
        OrderTable orderTable = OrderTable.of(1L, null, 3, false);
        assertThat(orderTable).isEqualTo(orderTable);
    }
}
