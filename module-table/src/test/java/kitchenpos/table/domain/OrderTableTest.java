package kitchenpos.table.domain;

import kitchenpos.table.domain.fixture.OrderTableFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.table.domain.OrderTable.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블")
class OrderTableTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {

        OrderTable orderTable = OrderTableFixture.notEmptyOrderTable();

        Assertions.assertAll(
                () -> assertThat(orderTable.getTableGroup()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(0)),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("공석 상태로 변경 / 테이블 그룹이 없을 수 없다.")
    @Test
    void empty_fail_tableGroup() {

        OrderTable orderTable = OrderTableFixture.notEmptyNotTableGroupOrderTable();

        assertThatThrownBy(orderTable::empty)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("공석 상태로 변경")
    @Test
    void empty_fail_cooking() {
        OrderTable orderTable = OrderTableFixture.notEmptyOrderTable();
        orderTable.empty();
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTable = OrderTableFixture.notEmptyNotTableGroupOrderTable();
        orderTable.unGroup();
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @DisplayName("손님수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {
        OrderTable orderTable = OrderTableFixture.notEmptyNotTableGroupOrderTable();
        orderTable.changeSitNumberOfGuest(new NumberOfGuests(1));
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(1));
    }

    @DisplayName("손님수를 변경한다 / 0명보다 작을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_minimumNumber() {
        OrderTable orderTable = OrderTableFixture.notEmptyNotTableGroupOrderTable();
        assertThatThrownBy(() -> orderTable.changeSitNumberOfGuest(new NumberOfGuests(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블이 공석 상태면 손님수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_fail_empty() {
        OrderTable orderTable = OrderTableFixture.emptyNotTableGroupOrderTable();
        assertThatThrownBy(() -> orderTable.changeSitNumberOfGuest(new NumberOfGuests(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(EMPTY_EXCEPTION_MESSAGE);
    }
}
