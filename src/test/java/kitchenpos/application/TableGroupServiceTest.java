package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private List<OrderTable> orderTables = new ArrayList<>();
    private TableGroup tableGroup;
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        orderTables.add(new OrderTable(1, true));
        orderTables.add(new OrderTable(2, true));
        orderTables.add(new OrderTable(3, true));
        tableGroup = new TableGroup(orderTables);

        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("테이블그룹 생성 테스트")
    @Test
    void createTest() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);
        when(orderTableDao.save(any())).thenReturn(any());

        TableGroup resultGroup = tableGroupService.create(tableGroup);

        assertThat(resultGroup.getOrderTables().size()).isEqualTo(tableGroup.getOrderTables().size());
        assertThat(resultGroup.getOrderTables()).containsExactlyElementsOf(orderTables);
    }

    @DisplayName("테이블그룹 해제 테스트")
    @Test
    void upgroupTest() {
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                any(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
                )
        )).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(any());
        orderTables.forEach(orderTable -> orderTable.setTableGroupId(1L));
        tableGroupService.ungroup(1L);
        orderTables.forEach(orderTable -> assertThat(orderTable.getTableGroupId()).isNull());
    }


}
