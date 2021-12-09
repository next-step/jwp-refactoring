package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @Mock
    TableGroupDao tableGroupDao;


    @DisplayName("단체를 지정한다.")
    @Test
    void createTest() {

        // given
        OrderTable orderTable1 = mock(OrderTable.class);
        when(orderTable1.getId()).thenReturn(1L);
        OrderTable orderTable2 = mock(OrderTable.class);
        when(orderTable2.getId()).thenReturn(2L);

        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        OrderTable savedOrderTable1 = mock(OrderTable.class);
        when(savedOrderTable1.isEmpty()).thenReturn(true);
        when(savedOrderTable1.getTableGroupId()).thenReturn(null);

        OrderTable savedOrderTable2 = mock(OrderTable.class);
        when(savedOrderTable2.isEmpty()).thenReturn(true);
        when(savedOrderTable2.getTableGroupId()).thenReturn(null);

        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()))).thenReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));

        TableGroup savedTableGroup = mock(TableGroup.class);
        when(savedTableGroup.getId()).thenReturn(1L);

        when(tableGroupDao.save(tableGroup)).thenReturn(savedTableGroup);

        OrderTable expectedOrderTable1 = mock(OrderTable.class);
        OrderTable expectedOrderTable2 = mock(OrderTable.class);

        when(savedTableGroup.getOrderTables()).thenReturn(Arrays.asList(expectedOrderTable1, expectedOrderTable2));


        TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        // when
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(createdTableGroup.getId()).isNotNull();
        assertThat(createdTableGroup.getOrderTables()).containsExactly(expectedOrderTable1, expectedOrderTable2);
    }


    @DisplayName("단체를 해제한다.")
   @Test
    void ungroupTest(){

        // given
        Long tableGroupId = 1L;

        OrderTable orderTable1 = mock(OrderTable.class);
        when(orderTable1.getId()).thenReturn(1L);
        OrderTable orderTable2 = mock(OrderTable.class);
        when(orderTable2.getId()).thenReturn(2L);

        TableGroup tableGroup = mock(TableGroup.class);
        when(tableGroup.getId()).thenReturn(tableGroupId);

        when(orderTableDao.findAllByTableGroupId(tableGroup.getId())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

        OrderTable expectedOrderTable1 = mock(OrderTable.class);
        when(expectedOrderTable1.getTableGroupId()).thenReturn(null);
        OrderTable expectedOrderTable2 = mock(OrderTable.class);
        when(expectedOrderTable2.getTableGroupId()).thenReturn(null);

        when(orderTableDao.save(orderTable1)).thenReturn(expectedOrderTable1);
        when(orderTableDao.save(orderTable2)).thenReturn(expectedOrderTable2);

        TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        assertThat(expectedOrderTable1.getTableGroupId()).isNull();
        assertThat(expectedOrderTable2.getTableGroupId()).isNull();

    }
}
