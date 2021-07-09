package kitchenpos.table.domain;

import kitchenpos.table.exception.EmptyException;
import kitchenpos.table.exception.IllegalNumberOfGuestsException;
import kitchenpos.table.exception.NullGroupIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        OrderTable orderTable = new OrderTable(null, 2, true);

        assertThat(orderTable).isEqualTo(new OrderTable(null, 2, true));
    }

    @DisplayName("주문 테이블 empty값을 변경한다.")
    @Test
    void changeEmpty() {
        boolean expectedEmpty = false;
        OrderTable orderTable = new OrderTable(null, 2, true);

        orderTable.changeEmpty(expectedEmpty);

        assertThat(orderTable.isEmpty()).isEqualTo(expectedEmpty);
    }

    @DisplayName("주문 테이블 empty값 변경을 실패한다. - 테이블 그룹 아이디가 null이 아니면 실패")
    @Test
    void fail_changeEmpty1() {
        boolean expectedEmpty = false;
        OrderTable orderTable = new OrderTable(1L, 2, true);

        assertThatThrownBy(() -> orderTable.changeEmpty(expectedEmpty))
                .isInstanceOf(NullGroupIdException.class);
    }

    @DisplayName("주문 테이블 손님 숫자값(NumberOfGuests)을 변경한다.")
    @Test
    void changeNumberOfGuests() {
        int expectedNumberOfGuests = 4;
        OrderTable orderTable = new OrderTable(null, 2, false);

        orderTable.changeNumberOfGuests(expectedNumberOfGuests);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
    }

    @DisplayName("주문 테이블 손님 숫자값(NumberOfGuests)을 변경을 실패한다. - 변경하려는 guests 숫자가 0보다 작을 경우 변경 실패")
    @Test
    void fail_changeNumberOfGuests1() {
        int expectedNumberOfGuests = -1;
        OrderTable orderTable = new OrderTable(null, 2, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(expectedNumberOfGuests))
                .isInstanceOf(IllegalNumberOfGuestsException.class);
    }

    @DisplayName("주문 테이블 손님 숫자값(NumberOfGuests)을 변경을 실패한다. - 기존 주문 테이블의 empty 값이 true이면 변경 실패")
    @Test
    void fail_changeNumberOfGuests2() {
        int expectedNumberOfGuests = 4;
        OrderTable orderTable = new OrderTable(null, 2, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(expectedNumberOfGuests))
                .isInstanceOf(EmptyException.class);
    }
}
