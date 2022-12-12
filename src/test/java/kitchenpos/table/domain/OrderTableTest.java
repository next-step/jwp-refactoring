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

    @DisplayName("요리중일 경우 변경할 수 없다.")
    @Test
    void name() {

        OrderTable orderTable = new OrderTable();

        assertThatThrownBy(orderTable::empty)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TABLE_GROUP_EMPTY_EXCEPTION_MESSAGE);
    }
}
