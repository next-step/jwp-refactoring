package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @Test
    void create() {
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        OrderTable orderTable2 = new OrderTable(1L, null, 4, false);

        TableGroup tableGroup = TableGroup.of(new OrderTables(Arrays.asList(orderTable, orderTable2)));

        assertThat(tableGroup).isNotNull();
    }

    @DisplayName("테이블 그룹 생성시, 주문 테이블이 2개 미만이면 예외를 던집니다")
    @Test
    void createWithOneOrderTable() {
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        OrderTables orderTables = new OrderTables(Collections.singletonList(orderTable));

        assertThatThrownBy(() -> TableGroup.of(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성시, 주문 테이블이 비어있을 경우 예외를 던집니다")
    @Test
    void createWithEmptyOrderTable() {
        OrderTables orderTables = new OrderTables(new ArrayList<>());

        assertThatThrownBy(() -> TableGroup.of(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
