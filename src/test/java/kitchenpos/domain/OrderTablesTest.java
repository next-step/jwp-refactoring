package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void given_CompletedOrder_when_Ungroup_then_UngroupTable() {
        // given
        final TableGroup tableGroup = mock(TableGroup.class);
        final OrderTable orderTable1 = mock(OrderTable.class);
        final OrderTable orderTable2 = mock(OrderTable.class);
        final OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));
        List<OrderLineItem> orderLineItems = Collections.emptyList();
        final Order order1 = new Order(orderTable1, OrderStatus.COMPLETION, orderLineItems);
        final Order order2 = new Order(orderTable2, OrderStatus.COMPLETION, orderLineItems);
        Orders orders = new Orders(Arrays.asList(order1, order2));
        given(tableGroup.getId()).willReturn(1L);

        // when
        orderTables.ungroup(orders);

        // then
        verify(orderTable1).ungroup();
        verify(orderTable2).ungroup();
    }

    @Test
    void given_NotCompletedOrder_when_Ungroup_then_ThrowException() {
        // given
        final OrderTable orderTable1 = mock(OrderTable.class);
        final OrderTable orderTable2 = mock(OrderTable.class);
        final OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));
        List<OrderLineItem> orderLineItems = Collections.emptyList();
        final Order order1 = new Order(orderTable1, OrderStatus.COOKING, orderLineItems);
        final Order order2 = new Order(orderTable2, OrderStatus.COMPLETION, orderLineItems);
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
        final TableGroup tableGroup1 = mock(TableGroup.class);
        final TableGroup tableGroup2 = mock(TableGroup.class);
        final OrderTable orderTable1 = new OrderTable(tableGroup1, 1);
        final OrderTable orderTable2 = new OrderTable(tableGroup2, 2);
        List<OrderTable> orderTableList = Arrays.asList(orderTable1, orderTable2);
        final OrderTables orderTables = new OrderTables(orderTableList);

        // when
        orderTables.changeTableGroupId(2L);

        // then
        assertThat(orderTables.toList()).containsExactly(orderTable1, orderTable2);
        assertThat(orderTable1.isOccupied()).isEqualTo(true);
        assertThat(orderTable2.isOccupied()).isEqualTo(true);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.changeEmpty(true);

        // when
        final Throwable throwable = catchThrowable(() -> orderTable.changeNumberOfGuests(1));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
