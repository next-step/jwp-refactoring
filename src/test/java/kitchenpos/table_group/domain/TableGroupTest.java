package kitchenpos.table_group.domain;

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
        List<GroupTable> groupTables = singletonList(new GroupTable(1L, false, true));
        assertThatThrownBy(() -> TableGroup.group(groupTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아니면 단체 지정할 수 없다")
    @Test
    void not_empty_table() {
        GroupTable emptyTable = new GroupTable(1L, false, true);
        GroupTable notEmptyTable = new GroupTable(2L, false, false);
        List<GroupTable> groupTables = Arrays.asList(emptyTable, notEmptyTable);
        assertThatThrownBy(() -> TableGroup.group(groupTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체에 지정된 테이블은 단체 지정할 수 없다")
    @Test
    void grouped_table() {
        GroupTable groupedTable = new GroupTable(1L, true, true);
        GroupTable emptyTable = new GroupTable(2L, false, true);
        List<GroupTable> groupTables = Arrays.asList(groupedTable, emptyTable);
        assertThatThrownBy(() -> TableGroup.group(groupTables))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
