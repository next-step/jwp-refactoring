package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.table.domain.OrderTable.TABLE_GROUP_EMPTY_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블")
class OrderTableTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {

        OrderTable orderTable = new OrderTable();

        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isZero(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("공석 상태로 변경 / 테이블 그룹이 없을 수 없다.")
    @Test
    void empty_fail_tableGroup() {

        OrderTable orderTable = new OrderTable();

        assertThatThrownBy(orderTable::empty)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TABLE_GROUP_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("공석 상태로 변경")
    @Test
    void empty_fail_cooking() {

        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);

        assertThat(orderTable.isEmpty()).isFalse();

        orderTable.empty();
        assertThat(orderTable.isEmpty()).isTrue();
    }
}
