package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.persistence.OrderTableRepository;
import kitchenpos.table.persistence.TableGroupRepository;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;
    @Mock
    private OrderRepository orderDao;
    @Mock
    private OrderTableRepository orderTableDao;
    @Mock
    private TableGroupRepository tableGroupDao;

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneTable() {
        TableGroup tableGroup = TableGroup.builder()
                .orderTables(Collections.EMPTY_LIST)
                .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블이 2개미만이면 예외발생")
    @Test
    public void throwsExceptionWhenLessThen2Table() {
        TableGroup tableGroup = TableGroup.builder()
                .orderTables(Arrays.asList(OrderTable.builder().build()))
                .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 등록안된 테이블이 있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsTable() {
        List<OrderTable> orderTables = getOrderTables(OrderTable.builder().build(),3);
        TableGroup tableGroup = TableGroup.builder()
                .orderTables(orderTables)
                .build();
        doReturn(orderTables.subList(0, 2))
                .when(orderTableDao)
                .findAllById(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블이 공석이 아닌경우 예외발생")
    @Test
    public void throwsExceptionWhenNoneEmptyTable() {
        List<OrderTable> orderTables = getOrderTables(OrderTable.builder().build(),3);
        TableGroup tableGroup = TableGroup.builder().orderTables(orderTables).build();
        doReturn(orderTables)
                .when(orderTableDao)
                .findAllById(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블에 이미 등록된 그룹이 있으면 예외발생")
    @Test
    public void throwsExceptionWhenAlreadyHasGroup() {
        List<OrderTable> orderTables = getOrderTables(OrderTable.builder()
                .tableGroup(TableGroup.builder().id(Arbitraries.longs().greaterOrEqual(1).sample()).build())
                .build(),3);
        TableGroup tableGroup = TableGroup.builder().orderTables(orderTables).build();
        doReturn(orderTables)
                .when(orderTableDao)
                .findAllById(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 테이블그룹 반환")
    @Test
    public void returnTableGroup() {
        List<OrderTable> orderTables = getOrderTables(OrderTable
                .builder()
                .tableGroup(TableGroup.builder().build())
                .empty(true).build(),3);
        TableGroup tableGroup = TableGroup.builder().orderTables(orderTables).build();
        doReturn(orderTables)
                .when(orderTableDao)
                .findAllById(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));
        doReturn(tableGroup)
                .when(tableGroupDao)
                .save(tableGroup);
        doReturn(OrderTable.builder().build())
                .when(orderTableDao)
                .save(any(OrderTable.class));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        List<OrderTable> savedOrderTables = savedTableGroup.getOrderTables();

        assertAll(
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedOrderTables.stream().map(OrderTable::isEmpty).collect(Collectors.toList()))
                        .allMatch(isEmpty -> !isEmpty),
                () -> assertThat(savedOrderTables.stream().map(OrderTable::getTableGroupId).collect(Collectors.toList()))
                        .allMatch(tableGroupId -> Objects.equals(tableGroupId, savedTableGroup.getId())));
    }

    @Disabled
    @DisplayName("테이블그룹을 해제할경우 테이블이 조리중이거나 식사중이면 예외발생")
    @Test
    public void throwsExceptionWhenTableIsMillOrCOOKING() {
        List<OrderTable> orderTables = getOrderTables(OrderTable
                .builder()
                .id(Arbitraries.longs().between(1,100).sample())
                .tableGroup(TableGroup.builder().build())
                .empty(true).build(),3);
        List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
        TableGroup tableGroup = TableGroup.builder()
                .id(13l)
                .orderTables(orderTables).build();
        doReturn(Optional.ofNullable(tableGroup))
                .when(tableGroupDao)
                .findById(anyLong());
        doReturn(orderTables)
                .when(orderTableDao)
                .findAllByTableGroup(tableGroup);
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTable> getOrderTables(OrderTable orderTable, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> orderTable)
                .collect(Collectors.toList());
    }
}

