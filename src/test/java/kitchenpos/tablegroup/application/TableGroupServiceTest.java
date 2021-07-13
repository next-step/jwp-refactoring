package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.exception.IllegalOrderTableException;
import kitchenpos.table.exception.IllegalOrderTablesSizeException;
import kitchenpos.table.exception.NotInitOrderTablesException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTables orderTables;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 2, true);
        orderTable2 = new OrderTable(2L, null, 3, true);
        List<OrderTable> ordertableSamples = new ArrayList<>();
        ordertableSamples.add(orderTable1);
        ordertableSamples.add(orderTable2);
        orderTables = new OrderTables(ordertableSamples);
    }

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        TableGroup tableGroup = new TableGroup();
        TableGroupRequest request = new TableGroupRequest(orderTables.getOrderTables());
        given(orderTableRepository.findAllById(anyList())).willReturn(orderTables.getOrderTables());
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        TableGroupResponse created = tableGroupService.create(request);

        assertThat(created.getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
        assertThat(created.getOrderTables()).containsExactly(orderTable1, orderTable2);

        verify(orderTableRepository, times(1)).findAllById(anyList());
        verify(tableGroupRepository, times(1)).save(any());
    }

    @DisplayName("테이블 그룹 등록을 실패한다 - 등록에 요청할 주문 테이블 갯수가 없거나 2개 미만일 경우 실패")
    @Test
    void fail_create1() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1);
        TableGroupRequest request = new TableGroupRequest(orderTables);
        TableGroupRequest noneTableGroupRequest = new TableGroupRequest(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalOrderTablesSizeException.class);
        assertThatThrownBy(() -> tableGroupService.create(noneTableGroupRequest))
                .isInstanceOf(IllegalOrderTablesSizeException.class);
    }

    @DisplayName("테이블 그룹 등록을 실패한다 - 주문 테이블이 사전에 등록(주문값이 있는 상태) 되어 있지 않을 경우 실패")
    @Test
    void fail_create2() {
        TableGroupRequest request = new TableGroupRequest(orderTables.getOrderTables());
        given(orderTableRepository.findAllById(anyList())).willReturn(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(NotInitOrderTablesException.class);

        verify(orderTableRepository, times(1)).findAllById(anyList());
    }

    @DisplayName("테이블 그룹 등록을 실패한다 -  주문 테이블은 empty 값이 false일 경우 실패")
    @Test
    void fail_create3() {
        OrderTable orderTable1 = new OrderTable(1L, null, 2, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroupRequest request = new TableGroupRequest(orderTables);
        given(orderTableRepository.findAllById(anyList())).willReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalOrderTableException.class);

        verify(orderTableRepository, times(1)).findAllById(anyList());
    }

    @DisplayName("테이블 그룹 등록을 실패한다 -  주문 테이블 groupId 값이 있을 경우 실패")
    @Test
    void fail_create4() {
        OrderTable groupedTable1 = new OrderTable(orderTable1.getId(), 1L, 2, true);
        OrderTable groupedTable2 = new OrderTable(orderTable2.getId(), null, 3, true);
        List<OrderTable> groupedTables = Arrays.asList(groupedTable1, groupedTable2);
        TableGroupRequest request = new TableGroupRequest(groupedTables);
        given(orderTableRepository.findAllById(anyList())).willReturn(groupedTables);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalOrderTableException.class);

        verify(orderTableRepository, times(1)).findAllById(anyList());
    }

    @DisplayName("테이블 그룹을 등록해제(ungroup) 한다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(tableGroup.getId(), 2, true);
        orderTable1.addOrder(new Order(OrderStatus.COMPLETION));
        OrderTable orderTable2 = new OrderTable(tableGroup.getId(), 2, true);
        orderTable2.addOrder(new Order(OrderStatus.COMPLETION));
        given(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).willReturn(Arrays.asList(orderTable1, orderTable2));

        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();

        verify(orderTableRepository, times(1)).findAllByTableGroupId(tableGroup.getId());
    }

    @DisplayName("테이블 그룹 등록해제를 실패 한다. - 그룹된 주문 테이블이 조리중이거나, 식사중일때에는 그룹 해제 불가")
    @Test
    void fail_ungroup() {
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(tableGroup.getId(), 2, true);
        orderTable1.addOrder(new Order(OrderStatus.COOKING));
        OrderTable orderTable2 = new OrderTable(tableGroup.getId(), 2, true);
        orderTable2.addOrder(new Order(OrderStatus.COMPLETION));

        given(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).willReturn(Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalOrderTableException.class);

        verify(orderTableRepository, times(1)).findAllByTableGroupId(tableGroup.getId());
    }
}
