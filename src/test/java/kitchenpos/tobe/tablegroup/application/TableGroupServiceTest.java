package kitchenpos.tobe.tablegroup.application;

import kitchenpos.tobe.order.domain.OrderRepository;
import kitchenpos.tobe.order.domain.OrderStatus;
import kitchenpos.tobe.table.domain.OrderTable;
import kitchenpos.tobe.table.domain.OrderTableRepository;
import kitchenpos.tobe.table.domain.OrderTables;
import kitchenpos.tobe.table.dto.OrderTableRequest;
import kitchenpos.tobe.tablegroup.domain.TableGroup;
import kitchenpos.tobe.tablegroup.domain.TableGroupRepository;
import kitchenpos.tobe.tablegroup.dto.TableGroupRequest;
import kitchenpos.tobe.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @Test
    @DisplayName("테이블 그룹을 지정하고, 지정된 객체를 리턴한다")
    void create_table_group() {
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(1L);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(2L);
        List<OrderTableRequest> orderTableRequests = Arrays.asList(orderTableRequest1, orderTableRequest2);
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);
        TableGroup givenTableGroup = TableGroup.of(new OrderTables(orderTables));

        when(orderTableRepository.findAllById(anyList()))
                .thenReturn(orderTables);
        when(tableGroupRepository.save(any(TableGroup.class)))
                .thenReturn(givenTableGroup);
        TableGroupResponse actual = tableGroupService.create(tableGroupRequest);

        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("주문 테이블 없이 테이블 그룹을 지정하면 예외를 던진다.")
    void create_table_group_with_no_order_table() {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(new ArrayList<>());
        when(orderTableRepository.findAllById(anyList()))
                .thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 지정시 주문 테이블 1개만 주어지면 예외를 던진다.")
    void create_table_group_with_one_order_table() {
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(1L);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest1));
        when(orderTableRepository.findAllById(anyList()))
                .thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 저장시 그룹 지정할 주문 테이블 갯수가 다르게 주어지면 예외를 던진다")
    void create_table_group_with_different_order_table() {
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(1L);
        List<OrderTableRequest> orderTableRequests = Collections.singletonList(orderTableRequest1);
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);
        when(orderTableRepository.findAllById(anyList()))
                .thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 저장시 주문 테이블이 비어있지 않으면 예외를 던진다.")
    void create_table_group_with_not_empty_order_table() {
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(1L);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(2L);
        List<OrderTableRequest> orderTableRequests = Arrays.asList(orderTableRequest1, orderTableRequest2);
        OrderTable orderTable1 = new OrderTable(1L, null, 3, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

        when(orderTableRepository.findAllById(anyList()))
                .thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalStateException.class);
    }


    @Test
    @DisplayName("지정할 주문 테이블이 테이블 그룹에 이미 포함되어있으면 예외를 던진다.")
    void create_table_group_with_order_table_has_group_id() {
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(1L);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(2L);
        List<OrderTableRequest> orderTableRequests = Arrays.asList(orderTableRequest1, orderTableRequest2);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

        OrderTable savedOrderTable = new OrderTable(1L, null, 4, false);
        OrderTable savedOrderTable2 = new OrderTable(1L, null, 4, false);
        TableGroup tableGroup = TableGroup.of(new OrderTables(Arrays.asList(savedOrderTable, savedOrderTable2)));

        OrderTable orderTable1 = new OrderTable(1L, tableGroup, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        when(orderTableRepository.findAllById(anyList()))
                .thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다")
    void ungroup() {
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, true);
        Long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup givenTableGroup = TableGroup.of(new OrderTables(orderTables));
        when(tableGroupRepository.findById(anyLong()))
                .thenReturn(Optional.of(givenTableGroup));

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
    }

    @Test
    @DisplayName("테이블 그룹을 해제할 주문테이블이 완료 상태가 아니면 예외를 던진다.")
    void ungroup_table_with_table_status_is_not_complete() {
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, true);
        Long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup givenTableGroup = TableGroup.of(new OrderTables(orderTables));
        when(tableGroupRepository.findById(anyLong()))
                .thenReturn(Optional.of(givenTableGroup));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                anyList(), eq(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
        ).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalStateException.class);
    }
}
