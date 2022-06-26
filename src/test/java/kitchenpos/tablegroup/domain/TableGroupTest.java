package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {
    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void group() {
        //given
        OrderTables orderTables = new OrderTables(
                2,
                Arrays.asList(
                        new OrderTable(1L, null, 5, true),
                        new OrderTable(2L, null, 10, true)
                )
        );

        //when
        TableGroup tableGroup = TableGroup.group(orderTables);

        //then
        assertThat(tableGroup.getOrderTables().stream().map(OrderTable::getNumberOfGuests)).containsExactlyInAnyOrder(
                10, 5);
    }

    @Test
    @DisplayName("단체 지정을 해제 할 수 있다.")
    void ungroup() {
        //given
        TableGroup tableGroup = TableGroup.empty();
        OrderTable orderTable1 = new OrderTable(1L, tableGroup, 5, false);
        OrderTable orderTable2 = new OrderTable(2L, tableGroup, 1, false);
        tableGroup.add(orderTable1);
        tableGroup.add(orderTable2);

        //when
        tableGroup.unGroup();

        //then
        assertThat(tableGroup.getOrderTables().stream().noneMatch(OrderTable::hasTableGroup)).isTrue();
    }

    @Test
    @DisplayName("단체 지정 내 속한 주문 테이블 중 이미 단체 지정이 되어있거나, 빈 테이블이 아닌 주문 테이블이 있으면 단체 지정에 실패한다.")
    void create_failed() {
        //given
        OrderTables orderTables = new OrderTables(
                2,
                Arrays.asList(
                        new OrderTable(1L, TableGroup.empty(), 5, false),
                        new OrderTable(2L, null, 10, true)
                )
        );
        //then
        assertThatThrownBy(() -> TableGroup.group(orderTables)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

}
