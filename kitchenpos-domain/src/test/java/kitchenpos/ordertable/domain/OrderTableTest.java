package kitchenpos.ordertable.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void constructor() {
        // when
        OrderTable orderTable = new OrderTable(10, true);

        // then
        assertThat(orderTable).isNotNull();
    }

    @DisplayName("주문 테이블의 그룹 아이디를 리셋한다.")
    @Test
    void ungroupTable() {
        // when
        OrderTable orderTable = new OrderTable(10, true);
        orderTable.ungroupTable(false);

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}
