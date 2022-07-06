package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {
    @Test
    @DisplayName("최소 2개 이상 테이블이어야만 단체 지정이 가능하다.")
    void createTableGroupFail() {
        OrderTable orderTable = OrderTable.of(4, false);
        OrderTable otherTable = OrderTable.of(4, false);
        OrderTables orderTables = OrderTables.of(Lists.list(orderTable, otherTable));
        TableGroup tableGroup = new TableGroup();

        tableGroup.addOrderTables(orderTables);

        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }
}
