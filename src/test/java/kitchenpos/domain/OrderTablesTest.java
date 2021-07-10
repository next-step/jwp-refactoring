package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void given_CompletedOrder_when_Ungroup_then_UngroupTable() {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, 2);
        final OrderTable orderTable2 = new OrderTable(1L, 3);
        final OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));
        final Order order1 = new Order(1L, OrderStatus.COMPLETION.name(), null);
        final Order order2 = new Order(2L, OrderStatus.COMPLETION.name(), null);
        Orders orders = new Orders(Arrays.asList(order1, order2));

        // when
        orderTables.ungroup(orders);

        // then
        assertThat(orderTables.tableGroupIds()).isEqualTo(Arrays.asList(null, null));
    }

    @Test
    void given_NotCompletedOrder_when_Ungroup_then_ThrowException() {
        // given
        final OrderTable orderTable1 = mock(OrderTable.class);
        final OrderTable orderTable2 = mock(OrderTable.class);
        final OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));
        final Order order1 = new Order(1L, OrderStatus.COOKING.name(), null);
        final Order order2 = new Order(2L, OrderStatus.COMPLETION.name(), null);
        Orders orders = new Orders(Arrays.asList(order1, order2));
        given(orderTable1.getId()).willReturn(1L);
        given(orderTable2.getId()).willReturn(2L);

        // when
        final Throwable throwable = catchThrowable(() -> orderTables.ungroup(orders));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeTableGroupId() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(new OrderTable(1L, 1), new OrderTable(1L, 2));
        final OrderTables orderTables = new OrderTables(orderTableList);

        // when
        orderTables.changeTableGroupId(2L);

        // then
        final OrderTable orderTable1 = new OrderTable(2L, 1);
        final OrderTable orderTable2 = new OrderTable(2L, 2);
        assertThat(orderTables.toList()).containsExactly(orderTable1, orderTable2);
        assertThat(orderTable1.isOccupied()).isEqualTo(true);
        assertThat(orderTable2.isOccupied()).isEqualTo(true);
    }
}
