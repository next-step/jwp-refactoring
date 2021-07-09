package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

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

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 1, true);
        orderTable2 = new OrderTable(2L, null, 1, true);
        orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
        tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
    }

    @DisplayName("단체 지정을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(toList());
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(orderTables);
        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);
        given(orderTableRepository.save(orderTable1)).willReturn(orderTable1);

        // when
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(createdTableGroup.getId()).isEqualTo(tableGroup.getId());
    }

    @DisplayName("단체 지정의 주문 테이블 목록이 올바르지 않으면 등록할 수 없다 : 주문 테이블 목록은 2개 이상이어야 한다.")
    @Test
    void createTest_orderTablesSize_lessThanTwo() {
        // given
        orderTables.remove(orderTables.size() - 1);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정의 주문 테이블 목록이 올바르지 않으면 등록할 수 없다 : 주문 테이블 목록은 등록되어있고 중복되지않은 주문 테이블 이어야 한다.")
    @Test
    void createTest_wrongOrderTable() {
        // given
        List<OrderTable> notDuplicatedOrderTable = new ArrayList<>(orderTables);
        orderTables.add(new OrderTable(2L, null, 1, true));
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(toList());
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(notDuplicatedOrderTable);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정의 주문 테이블 목록이 올바르지 않으면 등록할 수 없다 : 주문 테이블은 이미 다른 단체 지정에 등록되어있지 않아야 한다.")
    @Test
    void createTest_wrongOrderTable2() {
        // given
        orderTables.add(new OrderTable(3L, 1L, 1, true));
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(toList());
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(orderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroupTest() {
        // given
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable2.setTableGroupId(tableGroup.getId());

        given(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(false);
        given(orderTableRepository.save(orderTable1)).willReturn(orderTable1);
        given(orderTableRepository.save(orderTable2)).willReturn(orderTable2);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    @DisplayName("단체 지정된 주문 테이블 목록의 주문의 상태가 완료이어야 한다.")
    @Test
    void ungroupTest_wrongStatus() {
        // given
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        given(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
