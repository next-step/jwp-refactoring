package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        List<OrderTable> savedOrderTables = Arrays.asList(orderTable, orderTable2);
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(savedOrderTables);

        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setId(1L);
        when(tableGroupDao.save(tableGroup)).thenReturn(savedTableGroup);

        // when
        tableGroupService.create(tableGroup);

        // then
        verify(tableGroupDao).save(tableGroup);
        verify(orderTableDao).save(orderTable);
        verify(orderTableDao).save(orderTable2);
        assertThat(savedTableGroup.getOrderTables()).isEqualTo(savedOrderTables);
    }

    @Test
    void given_InvalidTableGroup_when_Create_then_ThrownException() {
        // given
        TableGroup emptyOrderTable = new TableGroup();
        // when
        final Throwable emptyOrderTableException = catchThrowable(() -> tableGroupService.create(emptyOrderTable));
        // then
        assertThat(emptyOrderTableException).isInstanceOf(IllegalArgumentException.class);

        // given
        TableGroup oneOrderTable = new TableGroup();
        oneOrderTable.setOrderTables(Collections.singletonList(new OrderTable()));
        // when
        final Throwable oneOrderTableException = catchThrowable(() -> tableGroupService.create(oneOrderTable));
        // then
        assertThat(oneOrderTableException).isInstanceOf(IllegalArgumentException.class);

        // given
        TableGroup twoOrderTables = new TableGroup();
        twoOrderTables.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Collections.singletonList(new OrderTable()));
        // when
        final Throwable differentTableSize = catchThrowable(() -> tableGroupService.create(twoOrderTables));
        // then
        assertThat(differentTableSize).isInstanceOf(IllegalArgumentException.class);

        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(new OrderTable(), new OrderTable()));
        // when
        final Throwable notEmptyOrderTable = catchThrowable(() -> tableGroupService.create(tableGroup));
        // then
        assertThat(notEmptyOrderTable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(orderTableDao).save(orderTable);
        verify(orderTableDao).save(orderTable2);
    }
}
