package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.*;
import java.util.*;
import java.util.Arrays;
import java.util.stream.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import kitchenpos.dao.*;
import kitchenpos.domain.*;

@DisplayName("단체 지정 관련 테스트")
class TableGroupServiceTest {
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;
    private OrderTable orderTable;
    private List<OrderTable> orderTables;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderDao = mock(OrderDao.class);
        orderTableDao = mock(OrderTableDao.class);
        tableGroupDao = mock(TableGroupDao.class);
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        tableGroup = TableGroup.of(1L, LocalDateTime.now(), null);

        orderTables = Lists.newArrayList(
            OrderTable.of(1L, null, 4, true),
            OrderTable.of(2L, null, 6, true)
        );
        tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTables);
    }

    @DisplayName("단체 지정 생성하기")
    @Test
    void createTest() {
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);
        when(orderTableDao.findAllByIdIn(Lists.newArrayList(1L, 2L))).thenReturn(orderTables);
        assertThat(tableGroupService.create(tableGroup)).isEqualTo(tableGroup);
    }

    @DisplayName("단체 지정 생성시 등록된 주문 테이블 개수가 1개이하면 예외 발생")
    @Test
    void exceptionTest1() {
        orderTables = Lists.newArrayList(OrderTable.of(1L, null, 4, true));
        tableGroup.setOrderTables(orderTables);
        assertThatThrownBy(
            () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정에 등록된 주문 테이블의 개수와 저장된 주문 테이블의 개수가 일치하지 않으면, 예외 발생")
    @Test
    void exceptionTest2() {
        List<OrderTable> threeOrderTables = Lists.newArrayList(
            OrderTable.of(1L, null, 4, true),
            OrderTable.of(2L, null, 4, true),
            OrderTable.of(3L, null, 4, true)
        );
        tableGroup.setOrderTables(threeOrderTables);

        assertThatThrownBy(
            () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 주문 테이블의 객체가 없으면 예외 발생")
    @Test
    void exceptionTest3() {
        when(orderTableDao.findAllByIdIn(Lists.newArrayList(1L, 2L))).thenReturn(
            Lists.newArrayList(
                OrderTable.of(1L, null, 4, true),
                new OrderTable()
            )
        );

        assertThatThrownBy(
            () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroupTest() {
        List<OrderTable> threeOrderTables = Lists.newArrayList(
            OrderTable.of(1L, tableGroup.getId(), 4, true),
            OrderTable.of(2L, tableGroup.getId(), 4, true),
            OrderTable.of(3L, tableGroup.getId(), 4, true)
        );
        tableGroup.setOrderTables(threeOrderTables);
        tableGroupService.ungroup(tableGroup.getId());
    }

    @DisplayName("단체 지정 해제시, 주분 테이블 상태가 조리 또는 식사중이면 예외 발생")
    @Test
    void exceptionTest4() {
        orderTables = Lists.newArrayList(
            OrderTable.of(1L, tableGroup.getId(), 4, true),
            OrderTable.of(2L, tableGroup.getId(), 6, true)
        );

        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        when(orderTableDao.findAllByTableGroupId(tableGroup.getId())).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).thenReturn(true);

        assertThatThrownBy(
            () -> tableGroupService.ungroup(tableGroup.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
