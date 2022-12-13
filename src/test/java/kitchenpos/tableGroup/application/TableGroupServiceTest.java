package kitchenpos.tableGroup.application;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    private static final List<String> NOT_COMPLETED_STATUSES = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("테이블그룹 생성 테스트")
    @Test
    void createTableGroupTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, 4, true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, 4, true);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));

        when(orderTableDao.findAllByIdIn(orderTablesToIds(테이블그룹.getOrderTables())))
                .thenReturn(테이블그룹.getOrderTables());
        when(tableGroupDao.save(테이블그룹)).thenReturn(테이블그룹);
        for (final OrderTable savedOrderTable : 테이블그룹.getOrderTables()) {
            when(orderTableDao.save(savedOrderTable)).thenReturn(savedOrderTable);
        }

        //when
        TableGroup result = tableGroupService.create(테이블그룹);

        //then
        assertThat(result).isEqualTo(테이블그룹);
        assertThat(orderTablesToIds(result.getOrderTables()))
                .allMatch(테이블그룹.getId()::equals);
        assertThat(orderTablesToEmpty(result.getOrderTables()))
                .allMatch(empty -> !empty);
    }

    @DisplayName("테이블 목록이 비어있을 경우 테이블그룹 생성 오류 발생")
    @Test
    void createTableGroupSizeZeroExceptionTest() {
        //given
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList());

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 목록 개수가 1인 경우 테이블그룹 생성 오류 발생")
    @Test
    void createTableGroupSizeOneExceptionTest() {
        //given
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(),
                Arrays.asList(new OrderTable(1L, null, 4, true)));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 목록에 있는 테이블이 존재하지 않는 경우 테이블그룹 생성 오류 발생")
    @Test
    void createTableGroupContainNotExistTableExceptionTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, 4, true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, 4, true);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));

        when(orderTableDao.findAllByIdIn(orderTablesToIds(테이블그룹.getOrderTables())))
                .thenReturn(Arrays.asList(주문테이블1));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 테이블로 테이블그룹 생성할 경우 오류 발생 테스트")
    @Test
    void createTableGroupEmptyTableExceptionTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, 4, false);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, 4, false);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));

        when(orderTableDao.findAllByIdIn(orderTablesToIds(테이블그룹.getOrderTables())))
                .thenReturn(테이블그룹.getOrderTables());

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹이 이미 존재하는 테이블로 테이블그룹을 생성할 경우 오류 발생 테스트")
    @Test
    void createTableGroupAlreadyExistTableGroupTableExceptionTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, 1L, 4, true);
        final OrderTable 주문테이블2 = new OrderTable(2L, 1L, 4, true);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));

        when(orderTableDao.findAllByIdIn(orderTablesToIds(테이블그룹.getOrderTables())))
                .thenReturn(테이블그룹.getOrderTables());

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void unGroupTableTest() {
        //given
        final Long tableGroupId = 1L;
        final List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1L, tableGroupId, 4, false),
                new OrderTable(2L, tableGroupId, 4, false));

        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTablesToIds(orderTables), NOT_COMPLETED_STATUSES))
                .thenReturn(false);
        for (final OrderTable orderTable : orderTables) {
            when(orderTableDao.save(orderTable)).thenReturn(orderTable);
        }

        //when
        tableGroupService.ungroup(tableGroupId);

        //then
        assertThat(orderTables.stream().map(OrderTable::getTableGroupId).collect(Collectors.toList()))
                .allMatch(id -> id == null);
    }

    @DisplayName("조리중이거나 식사중인 테이블을 테이블그룹에서 해제할 경우 오류발생 테스트")
    @Test
    void createTableGroupContainCookingOrMealTableExceptionTest() {
        //given
        final Long tableGroupId = 1L;
        final List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1L, tableGroupId, 4, false),
                new OrderTable(2L, tableGroupId, 4, false));

        when(orderTableDao.findAllByTableGroupId(tableGroupId))
                .thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTablesToIds(orderTables), NOT_COMPLETED_STATUSES))
                .thenReturn(true);

        //when
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<Long> orderTablesToIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private List<Boolean> orderTablesToEmpty(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::isEmpty)
                .collect(Collectors.toList());
    }
}
