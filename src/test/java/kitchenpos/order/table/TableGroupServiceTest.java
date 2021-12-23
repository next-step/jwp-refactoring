package kitchenpos.order.table;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;



    @DisplayName("단체 테이블 지정하기")
    @Test
    void groupingTable() {

        //given
        OrderTable orderTableA = new OrderTable();
        orderTableA.setId(1L);
        orderTableA.setNumberOfGuests(10);
        orderTableA.setEmpty(true);

        OrderTable orderTableB = new OrderTable();
        orderTableB.setId(2L);
        orderTableB.setNumberOfGuests(4);
        orderTableB.setEmpty(true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        List<OrderTable> orderTables = Arrays.asList(orderTableA, orderTableB);
        tableGroup.setOrderTables(orderTables);

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(orderTables);
        when(tableGroupDao.save(any())).thenReturn(tableGroup);
        when(orderTableDao.save(orderTableA)).thenReturn(orderTableA);
        when(orderTableDao.save(orderTableB)).thenReturn(orderTableB);

        //when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(savedTableGroup).isNotNull();
        assertThat(savedTableGroup.getId()).isGreaterThan(0L);
        assertThat(savedTableGroup.getOrderTables().stream()
                .map(OrderTable::isEmpty))
                .contains(false, false);

        assertThat(savedTableGroup.getOrderTables().stream()
                .map(OrderTable::getNumberOfGuests))
                .contains(10, 4);
    }

    @DisplayName("지정된 단체 테이블 해제하기")
    @Test
    void unGroupingTable() {

        //given
        final boolean orderTableStatusByCookingAndMeal = false;
        OrderTable orderTableA = new OrderTable();
        orderTableA.setId(1L);
        orderTableA.setNumberOfGuests(10);
        orderTableA.setEmpty(true);

        OrderTable orderTableB = new OrderTable();
        orderTableB.setId(2L);
        orderTableB.setNumberOfGuests(4);
        orderTableB.setEmpty(true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        List<OrderTable> orderTables = Arrays.asList(orderTableA, orderTableB);
        tableGroup.setOrderTables(orderTables);

        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(orderTableStatusByCookingAndMeal);
        when(orderTableDao.save(orderTableA)).thenReturn(orderTableA);
        when(orderTableDao.save(orderTableB)).thenReturn(orderTableB);

        //when
        tableGroupService.ungroup(tableGroup.getId());

        //then
        assertThat(orderTableA.getTableGroupId()).isNull();
        assertThat(orderTableB.getTableGroupId()).isNull();
    }

}
