package kitchenpos.ordertable.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTableTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void constructor() {
        // when
        OrderTable orderTable = new OrderTable(1L, 10, true);

        // then
        assertThat(orderTable).isNotNull();
        assertThat(orderTable.getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("주문 테이블의 그룹 아이디를 셋팅한다.")
    @Test
    void setTableGroupId() {
        // when
        OrderTable orderTable = new OrderTable(1L, 10, true);
        orderTable.setTableGroupId(2L);

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(2L);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블의 그룹 아이디를 리셋한다.")
    @Test
    void ungroupTable() {
        // when
        OrderTable orderTable = new OrderTable(1L, 10, true);
        orderTable.ungroupTable();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}
