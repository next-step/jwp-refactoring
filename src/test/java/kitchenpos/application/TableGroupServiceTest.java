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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    //그룹핑시
    @DisplayName("테이블그룹 생성 예외테스트: 테이블이 2개이하인 경우")
    @Test
    void notEnoughTable() {
        orderTables.remove(0);
        orderTables.remove(1);
        tableGroup.setOrderTables(orderTables);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroup)
        );

        assertThat(exception.getMessage()).isEqualTo("그룹핑할 테이블이 부족합니다.");
    }
    @DisplayName("테이블그룹 생성 예외테스트: 조회한 테이블 갯수가 일치하지 않는 경우")
    @Test
    void notMatchedTableSize() {
        List<OrderTable> findOrderTables = new ArrayList<>();
        findOrderTables.add(new OrderTable(1, true));
        findOrderTables.add(new OrderTable(2, true));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(findOrderTables);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroup)
        );

        assertThat(exception.getMessage()).isEqualTo("테이블 갯수가 일치하지 않습니다.");
    }
    @DisplayName("테이블그룹 생성 예외테스트: 테이블이 비어있지 않은 경우")
    @Test
    void notEmptyTable() {
        orderTables.get(0).setEmpty(false);
        when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroup)
        );

        assertThat(exception.getMessage()).isEqualTo("테이블이 사용중이거나 이미 그룹핑되어 있습니다.");
    }

    @DisplayName("테이블그룹 생성 예외테스트: 테이블에 그룹아이디가 있는경우")
    @Test
    void existGroupId() {
        orderTables.get(0).setTableGroupId(1L);
        when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroup)
        );

        assertThat(exception.getMessage()).isEqualTo("테이블이 사용중이거나 이미 그룹핑되어 있습니다.");
    }



    @DisplayName("테이블그룹 해제 테스트")
    @Test
    void upgroupTest() {
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                any(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        )).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(any());
        orderTables.forEach(orderTable -> orderTable.setTableGroupId(1L));

        tableGroupService.ungroup(1L);

        orderTables.forEach(orderTable -> assertThat(orderTable.getTableGroupId()).isNull());
    }

    @DisplayName("테이블그룹 해제 예외테스트: 테이블상태가 유효하지 않은 경우")
    @Test
    void invalidTableStatus() {
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                any(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        )).thenReturn(true);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.ungroup(1L)
        );

        assertThat(exception.getMessage()).isEqualTo("주문이 완료되지 않았습니다.");

    }

}
