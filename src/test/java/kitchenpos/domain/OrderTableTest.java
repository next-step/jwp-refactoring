package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 테스트")
class OrderTableTest {

    @DisplayName("id가 같은 두 객체는 동등하다.")
    @Test
    void equalsTest() {
        OrderTable table1 = OrderTable.of(1L, 1, false);
        OrderTable table2 = OrderTable.of(1L, 1, false);

        Assertions.assertThat(table1).isEqualTo(table2);
    }

    @DisplayName("id가 다르면 두 객체는 동등하지 않다.")
    @Test
    void equalsTest2() {
        OrderTable table1 = OrderTable.of(1L, 1, false);
        OrderTable table2 = OrderTable.of(2L, 1, false);

        Assertions.assertThat(table1).isNotEqualTo(table2);
    }
}
