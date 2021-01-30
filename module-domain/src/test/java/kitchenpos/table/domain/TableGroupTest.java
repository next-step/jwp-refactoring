package kitchenpos.table.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TableGroupTest {
    @Test
    @DisplayName("그룹으로 지정된 테이블들 그룹 해재")
    void unGroup() {
        OrderTable table1 = new OrderTable(1L, 2, true, Arrays.asList(new Order(OrderStatus.COMPLETION)));
        OrderTable table2 = new OrderTable(1L, 3, true, Arrays.asList(new Order(OrderStatus.COMPLETION)));
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup tableGroup = new TableGroup(orderTables);

        tableGroup.unGroup();

        assertThat(tableGroup.getOrderTables().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("주문상태가 COMPLETE가 아니면 그룹해제 불가")
    void cantUnGroupWhenOrderStatusNotCOMPLETE() {
        OrderTable table1 = new OrderTable(1L, 2, true);
        OrderTable table2 = new OrderTable(1L, 3, true);
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup tableGroup = new TableGroup(orderTables);

        assertThrows(IllegalArgumentException.class, tableGroup::unGroup);
    }
}