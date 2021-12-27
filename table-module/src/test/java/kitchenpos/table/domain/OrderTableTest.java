package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.OrderTable;

class OrderTableTest {
    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);

        // when
        orderTable.changeNumberOfGuests(2);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("방문한 손님 수를 음수로 변경하려 할 때 예외 발생")
    @Test
    void changeNegativeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTable.changeNumberOfGuests(-1))
            .withMessageContaining("방문한 손님 수는 0보다 작을 수 없습니다");
    }

    @DisplayName("빈 테이블일 때 방문한 손님 수를 변경하려 하면 예외 발생")
    @Test
    void changeNumberOfGuestsInEmptyTable() {
        // given
        final OrderTable orderTable = new OrderTable(1, true);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTable.changeNumberOfGuests(1))
            .withMessageContaining("빈 테이블의 방문한 손님 수를 변경할 수 없습니다");
    }

    @Test
    void ungroup() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 1, false);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}
