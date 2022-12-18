package kitchenpos.domain;

import kitchenpos.domain.type.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class TableGroupTest {

    @Test
    @DisplayName("단체 지정된 테이블을 해제할 수 있다.")
    void unGroup() {
        // given
        OrderTable orderTableA = new OrderTable(2, false);
        Order orderA = new Order(orderTableA, OrderStatus.COMPLETION);
        orderTableA.changeEmpty(true, Arrays.asList(orderA));

        OrderTable orderTableB = new OrderTable(4, false);
        Order orderB = new Order(orderTableB, OrderStatus.COMPLETION);
        orderTableB.changeEmpty(true, Arrays.asList(orderB));

        OrderTable orderTable1 = orderA.getOrderTable();
        OrderTable orderTable2 = orderB.getOrderTable();
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(orderTable1, orderTable2)));
        orderTable1.addTableGroup(tableGroup);

        tableGroup.ungroup(Arrays.asList(orderA));

        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }
}
