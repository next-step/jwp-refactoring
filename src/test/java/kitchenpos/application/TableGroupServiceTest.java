package kitchenpos.application;

import kitchenpos.fixture.TestOrderFactory;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("TableGroupService 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블을 단체 지정할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), true);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                new OrderTables(Arrays.asList(orderTable1, orderTable2))
        );
        TableGroupRequest request = TableGroupRequest.of(orderTableIds);

        when(orderTableRepository.findById(orderTable1.getId())).thenReturn(Optional.of(orderTable1));
        when(orderTableRepository.findById(orderTable2.getId())).thenReturn(Optional.of(orderTable2));
        when(tableGroupRepository.save(tableGroup)).thenReturn(tableGroup);

        // when
        TableGroupResponse result = tableGroupService.create(request);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(result.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("단체 지정 시, 주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void emptyOrderTableException() {
        // given
        TableGroupRequest request = TableGroupRequest.of(new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 주문 테이블이 2개보다 작을 경우 예외가 발생한다.")
    @Test
    void minimumOrderTableException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(orderTable1.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 주문 테이블이 등록되지 않은 경우 예외가 발생한다.")
    @Test
    void notExistOrderTableException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), true);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 빈 주문 테이블이 아닌 경우 예외가 발생한다.")
    @Test
    void notEmptyOrderTableException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), false);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), true);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 주문 테이블이 다른 단체에 지정되어 있다면 예외가 발생한다.")
    @Test
    void alreadyTableGroupException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), true);
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                new OrderTables(Arrays.asList(orderTable1, orderTable2))
        );
        orderTable1.setTableGroup(tableGroup);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void unTableGroup() {
        // given
        Order order1 = TestOrderFactory.createCompleteOrder();
        Order order2 = TestOrderFactory.createCompleteOrder();
        List<Long> orderTableIds = Arrays.asList(order1.getOrderTable().getId(), order2.getOrderTable().getId());
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                new OrderTables(Arrays.asList(order1.getOrderTable(), order2.getOrderTable()))
        );

        when(tableGroupRepository.findById(tableGroup.getId())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.findAllByOrderTableIdIn(orderTableIds)).thenReturn(Arrays.asList(order1, order2));
        when(tableGroupRepository.save(tableGroup)).thenReturn(tableGroup);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertAll(
                () -> assertThat(order1.getOrderTable().getTableGroup()).isNull(),
                () -> assertThat(order2.getOrderTable().getTableGroup()).isNull()
        );
    }

    @DisplayName("주문 테이블이 조리 or 식사 상태라면 단체 지정 해제 시 예외가 발생한다")
    @Test
    void unTableGroupException() {
        // given
        Order order1 = TestOrderFactory.createCompleteOrder();
        Order order2 = TestOrderFactory.createCompleteOrder();
        List<Long> orderTableIds = Arrays.asList(order1.getOrderTable().getId(), order2.getOrderTable().getId());
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                new OrderTables(Arrays.asList(order1.getOrderTable(), order2.getOrderTable()))
        );
        order1.setOrderStatus(OrderStatus.MEAL);

        when(tableGroupRepository.findById(tableGroup.getId())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.findAllByOrderTableIdIn(orderTableIds)).thenReturn(Arrays.asList(order1, order2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
