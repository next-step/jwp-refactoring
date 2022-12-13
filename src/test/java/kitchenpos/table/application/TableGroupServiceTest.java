package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.persistence.OrderTableRepository;
import kitchenpos.table.persistence.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupDao;
    @Mock
    private OrderRepository orderRepository;

    @DisplayName("테이블그룹을 추가할 경우 등록안된 테이블이 있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneTable() {
        TableGroupRequest tableGroup = new TableGroupRequest(Arrays.asList(1l, 2l, 3l, 4l, 5l));
        doReturn(Collections.EMPTY_LIST)
                .when(orderTableRepository)
                .findAllById(anyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블이 2개미만이면 예외발생")
    @Test
    public void throwsExceptionWhenLessThen2Table() {
        TableGroupRequest tableGroup = new TableGroupRequest(Arrays.asList(1l, 2l, 3l, 4l, 5l));
        doReturn(Arrays.asList(OrderTable.builder().id(1l).build()))
                .when(orderTableRepository)
                .findAllById(anyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블이 공석이 아닌경우 예외발생")
    @Test
    public void throwsExceptionWhenNoneEmptyTable() {
        TableGroupRequest tableGroup = new TableGroupRequest(Arrays.asList(1l, 2l, 3l, 4l, 5l));
        doReturn(Arrays.asList(OrderTable.builder().id(1l).build(), OrderTable.builder().id(2l).empty(true).build()))
                .when(orderTableRepository)
                .findAllById(anyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블에 이미 등록된 그룹이 있으면 예외발생")
    @Test
    public void throwsExceptionWhenAlreadyHasGroup() {
        TableGroupRequest tableGroup = new TableGroupRequest(Arrays.asList(1l, 2l, 3l, 4l, 5l));
        doReturn(Arrays.asList(OrderTable.builder().id(1l).tableGroup(TableGroup.builder().id(15l).build()).build(), OrderTable.builder().id(2l).empty(true).build()))
                .when(orderTableRepository)
                .findAllById(anyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 테이블그룹 반환")
    @Test
    public void returnTableGroup() {
        OrderTable orderTable1 = OrderTable.builder().id(1l).empty(true).build();
        OrderTable orderTable2 = OrderTable.builder().id(2l).empty(true).build();
        doReturn(Arrays.asList(orderTable1, orderTable2))
                .when(orderTableRepository)
                .findAllById(anyList());

        TableGroupResponse tableGroupResponse = tableGroupService.create(new TableGroupRequest(Arrays.asList(1l, 2l)));
        assertThat(tableGroupResponse.getOrderTableIds()).containsExactly(1l, 2l);
    }

    @DisplayName("테이블그룹을 해제할경우 테이블그룹이 등록안되있으면 예외발생")
    @Test
    public void throwsExceptionWhenTableGroupIsNull() {
        doReturn(Optional.empty())
                .when(tableGroupDao)
                .findById(anyLong());
        assertThatThrownBy(() -> tableGroupService.ungroup(13l))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 해제할경우 테이블에 포함된 주문이 조리중이거나 식사중이면 예외발생")
    @Test
    public void throwsExceptionWhenTableIsMillOrCOOKING() {
        List<Order> orders = Arrays.asList(Order.builder().orderTable(OrderTable.builder().build()).orderStatus(OrderStatus.COOKING).build());
        TableGroup tableGroup = TableGroup.builder()
                .orderTables(OrderTables.of(Arrays.asList(OrderTable.builder().build())))
                .build();
        doReturn(Optional.ofNullable(tableGroup))
                .when(tableGroupDao)
                .findById(anyLong());
        doReturn(orders)
                .when(orderRepository)
                .findAllByOrderTableIn(anyList());

        assertThatThrownBy(() -> tableGroupService.ungroup(15l))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

