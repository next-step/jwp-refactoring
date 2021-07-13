package kitchenpos.table.domain;

import kitchenpos.table.exception.EmptyException;
import kitchenpos.table.exception.IllegalNumberOfGuestsException;
import kitchenpos.table.exception.NoneNullGroupIdException;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
        OrderTable 주문테이블1 = new OrderTable();
        OrderTable 주문테이블2 = new OrderTable();
        TableGroup tableGroup = new TableGroup(1L, new OrderTables(Arrays.asList(주문테이블1, 주문테이블2)));
        OrderTable orderTable = new OrderTable(tableGroup.getId(), 2, true);

        assertThatThrownBy(() -> orderTable.changeEmpty(expectedEmpty))
                .isInstanceOf(NoneNullGroupIdException.class);
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
