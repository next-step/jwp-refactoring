package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단체 지정 단위테스트")
class TableGroupTest {
    @DisplayName("주문 테이블은 2개 이상이어야 한다")
    @Test
    void orderTable_size_2_over() {
        List<OrderTable> orderTables = singletonList(new OrderTable(0, true));
        assertThatThrownBy(() -> TableGroup.group(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아니면 단체 지정할 수 없다")
    @Test
    void not_empty_table() {
        OrderTable emptyTable = new OrderTable(1, true);
        OrderTable notEmptyTable = new OrderTable(1, false);
        List<OrderTable> orderTables = Arrays.asList(emptyTable, notEmptyTable);
        assertThatThrownBy(() -> TableGroup.group(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체에 지정된 테이블은 단체 지정할 수 없다")
    @Test
    void grouped_table() {
        OrderTable groupedTable = new OrderTable(1, true);
        groupedTable.groupBy(new TableGroup());
        OrderTable emptyTable = new OrderTable(1, true);
        List<OrderTable> orderTables = Arrays.asList(groupedTable, emptyTable);
        assertThatThrownBy(() -> TableGroup.group(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
