package kitchenpos.domain;

import static kitchenpos.domain.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("방문한 손님수 음수 방문한 손님수 변경 불가")
    @Test
    void changeNumberOfGuests_number_of_guest_less_then_0() {
        // given
        OrderTable orderTable = savedOrderTable(1L, 1L, 0, true);
        // when then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 테이블 방문한 손님수 변경 불가")
    @Test
    void changeNumberOfGuests_order_table_is_empty() {
        // given
        OrderTable orderTable = savedOrderTable(1L, 1L, 0, true);
        // when then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = savedOrderTable(1L, 1L, 0, false);
        // when
        orderTable.changeNumberOfGuests(5);
        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }
}
