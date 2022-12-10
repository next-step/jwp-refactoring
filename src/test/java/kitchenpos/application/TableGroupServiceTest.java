package kitchenpos.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
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
        OrderTable orderTable1 = new OrderTable(1L, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, 4, true);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));
        TableGroupRequest request = TableGroupRequest.of(orderTableIds);

        when(orderTableRepository.findAllByIdIn(orderTableIds)).thenReturn(Arrays.asList(orderTable1, orderTable2));
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
    void minumumOrderTableException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 4, true);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(orderTable1.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 주문 테이블이 등록되지 않은 경우 예외가 발생한다.")
    @Test
    void notExistOrderTableException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, 4, true);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 빈 주문 테이블이 아닌 경우 예외가 발생한다.")
    @Test
    void notEmptyOrderTableException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 4, false);
        OrderTable orderTable2 = new OrderTable(2L, 4, true);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 주문 테이블이 다른 단체에 지정되어 있다면 예외가 발생한다.")
    @Test
    void alreadyTableGroupException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, 4, true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));
        orderTable1.setTableGroup(tableGroup);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void unTableGroup() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, 4, true);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        when(orderTableRepository.findAllByTableGroupId(tableGroup.getId()))
                .thenReturn(Arrays.asList(orderTable1, orderTable2));

        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ).thenReturn(false);

        when(orderTableRepository.save(orderTable1)).thenReturn(orderTable1);
        when(orderTableRepository.save(orderTable2)).thenReturn(orderTable2);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable2.getTableGroup()).isNull()
        );
    }

    @DisplayName("주문 테이블이 조리 or 식사 상태라면 단체 지정 해제 시 예외가 발생한다")
    @Test
    void unTableGroupException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, 4, true);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        when(orderTableRepository.findAllByTableGroupId(tableGroup.getId()))
                .thenReturn(Arrays.asList(orderTable1, orderTable2));

        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
