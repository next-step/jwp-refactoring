package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void 단체_지정을_등록할_수_있다() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 10, true),
            new OrderTable(2L, null, 10, true));
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(orderTables);
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(savedTableGroup).isEqualTo(tableGroup);
    }

    @Test
    void 테이블을_단체로_2개_미만으로_단체_지정을_등록할_수_없다() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 10, true));
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    void 이미_단체로_지정되어_있는_주문테이블은_다른_단체로_지정_될_수_없다() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 1L, 10, true),
            new OrderTable(2L, null, 10, true));
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    void 비어_있지_않은_테이블은_단체로_지정될_수_없다() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 10, true),
            new OrderTable(2L, null, 10, true));
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(),
            Collections.singletonList(new OrderTable(1L, null, 10, true)));

        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    void 단체_지정을_해지할_수_있다() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 1L, 10, false),
            new OrderTable(2L, 1L, 10, false));
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(1L, 2L),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .thenReturn(false);

        tableGroupService.ungroup(1L);

        verify(orderTableDao).save(orderTables.get(0));
        verify(orderTableDao).save(orderTables.get(1));
    }
}
