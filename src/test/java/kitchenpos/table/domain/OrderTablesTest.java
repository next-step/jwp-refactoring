package kitchenpos.table.domain;

import static kitchenpos.table.domain.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {
    @DisplayName("비어있지 않은 주문 테이블 존재")
    @Test
    void group_order_table_not_empty_exists() {
        // given
        Long tableGroupId = 1L;
        OrderTables orderTables = OrderTables.from(Arrays.asList(
            savedOrderTable(1L, true),
            savedOrderTable(2L, false)
        ));

        // when, then
        assertThatThrownBy(() -> orderTables.group(tableGroupId))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("다른 단체 지정에 연결되어있는 주문 테이블 존재")
    @Test
    void group_order_table_table_group_id_exists() {
        // given
        Long tableGroupId = 1L;
        OrderTables orderTables = OrderTables.from(Arrays.asList(
            savedOrderTable(1L, true),
            savedOrderTable(2L, 1L, true)
        ));

        // when, then
        assertThatThrownBy(() -> orderTables.group(tableGroupId))
            .isInstanceOf(IllegalStateException.class);
    }
}
