package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 단위 테스트")
class TableGroupTest {

    @Test
    @DisplayName("주문을 그룹화한다.")
    void createOrderTableGroup() {
        OrderTable orderTable = Mockito.mock(OrderTable.class);
        OrderTable orderTable2 = Mockito.mock(OrderTable.class);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable, orderTable2));
        TableGroup tableGroup = new TableGroup(orderTables);

        assertThat(tableGroup.getOrderTables()).contains(orderTable, orderTable2);
    }

    @Test
    @DisplayName("주문을 그룹화하지 않는다.")
    void deleteOrderTableGroup() {
        OrderTable orderTable = Mockito.mock(OrderTable.class);
        OrderTable orderTable2 = Mockito.mock(OrderTable.class);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable, orderTable2));
        TableGroup tableGroup = new TableGroup(orderTables);

        tableGroup.cancleGroup();
        assertThat(tableGroup.getOrderTables().size()).isEqualTo(0);
    }
}
