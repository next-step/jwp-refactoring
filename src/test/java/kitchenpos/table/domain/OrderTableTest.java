package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.table.domain.OrderTable.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블")
class OrderTableTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        assertAll(
                () -> assertThat(orderTable.getTableGroup()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isZero(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

//    @DisplayName("공석 상태로 변경 / 테이블 그룹이 없을 수 없다.")
//    @Test
//    void empty_fail_tableGroup() {
//
//        OrderTable orderTable = new OrderTable();
//
//        assertThatThrownBy(orderTable::empty)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining(TABLE_GROUP_EMPTY_EXCEPTION_MESSAGE);
//    }

    @DisplayName("공석 상태로 변경")
    @Test
    void empty_fail_cooking() {

        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroup(new TableGroup());

        assertThat(orderTable.isEmpty()).isFalse();

        orderTable.empty();
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 1, true);
        assertThat(orderTable.getTableGroup()).isNotNull();
        orderTable.unGroup();
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @DisplayName("손님수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {
        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 1, false);
        orderTable.changeNumberOfGuests(1);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("손님수를 변경한다 / 0명보다 작을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_minimumNumber() {
        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 1, false);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
    }

//    @DisplayName("주문테이블이 없을 경우 손님수를 변경할 수 없다.")
//    @Test
//    void changeNumberOfGuests_fail_orderTable() {
//        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 1, false);
//        orderTable.setTableGroup(null);
//        assertThat(orderTable.getTableGroup()).isNull();
//        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining(ORDER_TABLE_NULL_EXCEPTION_MESSAGE);
//    }

    @DisplayName("테이블이 공석 상태면 손님수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_fail_empty() {
        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 1, true);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(EMPTY_EXCEPTION_MESSAGE);
    }
}
