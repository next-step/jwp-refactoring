package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.IllegalOrderTablesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @DisplayName("테이블이 없는 테이블 그룹을 생성한다")
    @Test
    void emptyOrderTableTest() {
        assertThatThrownBy(() -> new TableGroup(Collections.emptyList())).isInstanceOf(IllegalOrderTablesException.class);
    }

    @DisplayName("테이블이 한 개 있는 테이블 그룹을 생성한다")
    @Test
    void oneOrderTableTest() {
        OrderTable orderTable1 = new OrderTable(null, 1, true);
        assertThatThrownBy(() -> new TableGroup(Collections.singletonList(orderTable1))).isInstanceOf(IllegalOrderTablesException.class);
    }

    @DisplayName("빈 테이블이 아닌 테이블을 테이블 그룹으로 생성한다")
    @Test
    void NoEmptyTableTest() {
        OrderTable orderTable1 = new OrderTable(null, 1, true);
        OrderTable orderTable2 = new OrderTable(null, 1, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        assertThatThrownBy(() -> new TableGroup(orderTables)).isInstanceOf(IllegalOrderTablesException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 속해있는 테이블을 테이블 그룹으로 만든다")
    void alreadyTableGroupTest() {
        OrderTable orderTable1 = new OrderTable(new TableGroup(), 1, true);
        OrderTable orderTable2 = new OrderTable(null, 1, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        assertThatThrownBy(() -> new TableGroup(orderTables)).isInstanceOf(IllegalOrderTablesException.class);
    }
}