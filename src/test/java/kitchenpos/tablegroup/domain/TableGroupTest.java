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
        OrderTable orderTable1 = new OrderTable(4, true);
        OrderTable orderTable2 = new OrderTable(6, true);
        OrderTable orderTable3 = new OrderTable(8, true);
        TableGroup tableGroup = new TableGroup(OrderTables.from(Arrays.asList(orderTable1, orderTable2)));

        assertThatThrownBy(() -> new TableGroup(OrderTables.from(Arrays.asList(orderTable1, orderTable2, orderTable3))))
            .isInstanceOf(IllegalArgumentException.class);
    }
}