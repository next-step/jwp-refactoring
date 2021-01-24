package kitchenpos.table.application;

import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderTableService orderTableService;
    @InjectMocks
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository, orderTableService);
    }

    @DisplayName("2개 이상의 빈 테이블을 단체로 지정할 수 있다.")
    @Test
    void groupingTables() {
        //given
        OrderTable table1 = new OrderTable(1L, null, 4, true);
        OrderTable table2 = new OrderTable(2L, null, 4, true);
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup tableGroup = new TableGroup(1L, orderTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        given(orderTableService.findById(any())).willReturn(table1);

        //when
        List<OrderTableRequest> orderTableRequest = Arrays.asList(new OrderTableRequest(table1.getId()), new OrderTableRequest(table2.getId()));
        TableGroupRequest request = new TableGroupRequest(orderTableRequest);
        TableGroupResponse save = tableGroupService.create(request);

        //then
        assertThat(save.getOrderTables().get(0).getId()).isEqualTo(table1.getId());
        assertThat(save.getOrderTables().get(1).getId()).isEqualTo(table2.getId());
    }

    @DisplayName("1개 이하 빈 테이블의 단체션 지정시 익셉션")
    @Test
    void groupingOneTable() {
        //given
        OrderTable table1 = new OrderTable(1L, null, 4, true);

        //when
        List<OrderTableRequest> orderTableRequest = Arrays.asList(new OrderTableRequest(table1.getId()));
        TableGroupRequest request = new TableGroupRequest(orderTableRequest);
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정은 중복될 수 없다.")
    @Test
    void groupingTablesDuplicated() {
        //given
        OrderTable table1 = new OrderTable(1L, null, 4, true);
        List<OrderTable> orderTables = Arrays.asList(table1);

        //when
        List<OrderTableRequest> orderTableRequest = Arrays.asList(new OrderTableRequest(table1.getId()));
        TableGroupRequest request = new TableGroupRequest(orderTableRequest);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해지할 수 있다.")
    @Test
    void ungroupTables() {
        OrderTable table1 = new OrderTable(1L, null, 4, true);
        OrderTable table2 = new OrderTable(2L, null, 4, true);
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup tableGroup = new TableGroup(1L, orderTables);
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(orderTables);

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> founds = orderTableRepository.findAllByIdIn(Arrays.asList(table1.getId(), table2.getId()));
        assertThat(founds).doesNotContain(table1, table2);
    }

    @DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리인 경우 단체 지정을 해지할 수 없다.")
    @Test
    void cantUnGroupingWhenOrderStatus() {
        OrderTable table1 = new OrderTable(1L, null, 4, true);
        OrderTable table2 = new OrderTable(2L, null, 4, true);
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup tableGroup = new TableGroup(1L, orderTables);
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블 상태가 식사인 경우 단체 지정을 해지할 수 없다.")
    @Test
    void cantUnGroupingWhenMealStatus() {
        OrderTable table1 = new OrderTable(1L, null, 4, true);
        OrderTable table2 = new OrderTable(2L, null, 4, true);
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup tableGroup = new TableGroup(1L, orderTables);
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}