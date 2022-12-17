package kitchenpos.table.application;

import kitchenpos.order.exception.OrderException;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.persistence.OrderTableRepository;
import kitchenpos.table.persistence.TableGroupRepository;
import kitchenpos.table.validator.TableValidator;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TableValidator tableValidator;
    @Mock
    private OrderValidator orderValidator;

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
        doReturn(Arrays.asList(OrderTable.builder().id(1l).tableGroupId(15l).build(), OrderTable.builder().id(2l).empty(true).build()))
                .when(orderTableRepository)
                .findAllById(anyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 테이블그룹 반환")
    @Test
    public void returnTableGroup() {
        OrderTable orderTable1 = OrderTable.builder()
                .id(1l).empty(true).build();
        OrderTable orderTable2 = OrderTable.builder()
                .id(2l).empty(true).build();
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        doReturn(orderTables)
                .when(orderTableRepository)
                .findAllById(anyList());
        doReturn(TableGroup.builder()
                .id(1l)
                .build())
                .when(tableGroupRepository)
                .save(any(TableGroup.class));

        TableGroupResponse tableGroupResponse = tableGroupService.create(new TableGroupRequest(Arrays.asList(1l, 2l)));
        assertThat(tableGroupResponse.getOrderTables().stream().map(OrderTableResponse::getId).collect(Collectors.toList())).containsExactly(1l, 2l);
    }

    @DisplayName("테이블그룹을 해제할경우 테이블에 포함된 주문이 조리중이거나 식사중이면 예외발생")
    @Test
    public void throwsExceptionWhenTableIsMillOrCOOKING() {
        doThrow(new OrderException("계산이 끝나지 않은 주문은 상태를 변경할 수 없습니다"))
                .when(orderValidator)
                .validateOrderComplete(anyList());


        assertThatThrownBy(() -> tableGroupService.ungroup(15l))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("계산이 끝나지 않은 주문은 상태를 변경할 수 없습니다");
    }
}

