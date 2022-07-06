package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 단위테스트")
class OrderTableTest {
    @DisplayName("주문 테이블을 빈 테이블 상태로 변경할 수 있다")
    @Test
    void changeEmptyTrue() {
        OrderTable orderTable = new OrderTable(0, false);
        orderTable.changeEmpty(true);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블을 빈 테이블이 아닌 상태로 변경할 수 있다")
    @Test
    void changeEmptyFalse() {
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.changeEmpty(false);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("단체 지정된 주문 테이블은 빈 테이블 여부를 변경할 수 없다")
    @Test
    void groupedTable_is_cannot_change() {
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.groupBy(1L);
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경할 수 있다")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(0, false);
        orderTable.changeNumberOfGuests(2);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("방문한 손님 수는 0명 이상이어야 한다")
    @Test
    void number_is_0_over() {
        OrderTable orderTable = new OrderTable(0, false);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 테이블이면 방문한 손님 수를 변경할 수 없다")
    @Test
    void empty_table_cannot_change() {
        OrderTable orderTable = new OrderTable(0, true);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 단체 지정할 수 있다")
    @Test
    void group() {
        OrderTable orderTable = new OrderTable(0, true);
        Long tableGroupId = 1L;
        orderTable.groupBy(tableGroupId);
        assertAll(() -> {
            assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId);
            assertThat(orderTable.isEmpty()).isFalse();
        });
    }

    @DisplayName("주문 테이블을 단체 지정을 해제할 수 있다")
    @Test
    void ungroup() {
        OrderTable orderTable = new OrderTable(0, true);
        Long tableGroupId = 1L;
        orderTable.groupBy(tableGroupId);
        orderTable.ungroup();
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}
