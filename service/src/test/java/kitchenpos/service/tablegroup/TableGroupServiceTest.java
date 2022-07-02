package kitchenpos.service.tablegroup;

import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.service.table.dto.OrderTableRequest;
import kitchenpos.service.table.dto.OrderTableResponse;
import kitchenpos.service.tablegroup.application.TableGroupService;
import kitchenpos.service.tablegroup.dto.TableGroupRequest;
import kitchenpos.service.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void create() {
        //given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(
                Arrays.asList(OrderTable.of(5, true), OrderTable.of(1, true)));

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(
                new TableGroupRequest(Arrays.asList(new OrderTableRequest(1L), new OrderTableRequest(2L))));

        //then
        assertThat(savedTableGroup.getOrderTables().stream().map(OrderTableResponse::getNumberOfGuests)).isNotEmpty()
                .containsExactlyInAnyOrder(1, 5);
    }

    @Test
    @DisplayName("요청한 단체 지정의 주문 테이블 수와 실제 주문 테이블 갯수 차이가 나면 단체 지정에 실패한다.")
    void create_failed() {
        //given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Collections.emptyList());

        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L), new OrderTableRequest(2L))))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void ungroup() {
        //given
        OrderTable orderTable1 = OrderTable.of(5, true);
        OrderTable orderTable2 = OrderTable.of(1, true);
        TableGroup tableGroup = new TableGroup(new OrderTables(2, Arrays.asList(orderTable1, orderTable2)));
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        given(ordersRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        //when
        tableGroupService.ungroup(0L);

        //then
        assertThat(orderTable1.isGrouped()).isFalse();
        assertThat(orderTable2.isGrouped()).isFalse();
    }

    @Test
    @DisplayName("단체 지정 내 주문 상태가 조리 혹은 식사 인 주문 테이블이 포함되어 있을 경우 해제할 수 없다.")
    void ungroup_failed() {
        //given
        OrderTable orderTable1 = OrderTable.of(5, true);
        OrderTable orderTable2 = OrderTable.of(1, true);
        TableGroup tableGroup = new TableGroup(new OrderTables(2, Arrays.asList(orderTable1, orderTable2)));
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        given(ordersRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(0L)).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
