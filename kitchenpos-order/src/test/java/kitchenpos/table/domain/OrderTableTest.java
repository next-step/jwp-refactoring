package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {
    @DisplayName("빈 OrderTable을 생성한다.")
    @Test
    void createEmptyTable() {
        OrderTable orderTable = new OrderTable(0, true);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("비어있지 않은 OrderTable을 생성한다.")
    @Test
    void createNotEmptyTable() {
        OrderTable orderTable = new OrderTable(5, false);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("손님의 수가 -1인 OrderTable을 생성한다.")
    @Test
    void failureCreateWithNegativeNumberOfGuests() {
        assertThatThrownBy(() ->
            new OrderTable(-1, false)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비움 여부를 변경한다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = new OrderTable(0, false);

        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuest() {
        OrderTable orderTable = new OrderTable(0, false);

        orderTable.changeNumberOfGuests(5);

        assertThat(orderTable.getNumberOfGuests().value()).isEqualTo(5);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuestWithEmpty() {
        OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() ->
            orderTable.changeNumberOfGuests(5)
        ).isInstanceOf(IllegalStateException.class);
    }
}
