package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("이미 단체가 지정된 주문 테이블을 단체 지정하면 에러가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableAlreadyHasTableGroup() {
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(6, true);
        OrderTable orderTable3 = OrderTable.of(8, true);
        TableGroup tableGroup1 = TableGroup.from(1L);
        OrderTables.from(Arrays.asList(orderTable1, orderTable2)).registerTableGroup(tableGroup1.getId());
        TableGroup tableGroup2 = TableGroup.from(2L);

        assertThatThrownBy(() -> OrderTables.from(Arrays.asList(orderTable2, orderTable3)).registerTableGroup(tableGroup2.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}