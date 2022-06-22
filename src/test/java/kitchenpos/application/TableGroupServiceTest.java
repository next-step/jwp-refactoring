package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
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
        OrderTable orderTable1 = new OrderTable(1L, null, 5, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 1, true);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        TableGroup tableGroup = new TableGroup();
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L), new OrderTableRequest(2L))));

        //then
        assertThat(savedTableGroup.getOrderTables().stream().map(OrderTableResponse::getId)).isNotEmpty()
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("단체 지정에 속한 주문 테이블 수가 2 미만이면 실패한다.")
    void create_failed_1() {
        assertThatThrownBy(() -> tableGroupService.create(
                new TableGroupRequest(Arrays.asList(new OrderTableRequest(1L))))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("요청한 단체 지정의 주문 테이블 수와 실제 주문 테이블 갯수 차이가 나면 단체 지정에 실패한다.")
    void create_failed_2() {
        //given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Collections.emptyList());

        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L),
                        new OrderTableRequest(2L))))).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 내 속한 주문 테이블 중 이미 단체 지정이 되어있거나, 빈 테이블이 아닌 주문 테이블이 있으면 단체 지정에 실패한다.")
    void create_failed_3() {
        //given
        OrderTable emptyOrderTable = new OrderTable(1L, null, 5, true);
        OrderTable alreadyGroupedOrderTable = new OrderTable(2L, new TableGroup(), 5, false);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(emptyOrderTable, alreadyGroupedOrderTable));

        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L),
                        new OrderTableRequest(2L))))).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void ungroup() {
        //given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable(1L, tableGroup, 5, false);
        OrderTable orderTable2 = new OrderTable(2L, tableGroup, 1, false);
        tableGroup.add(orderTable1);
        tableGroup.add(orderTable2);
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        given(ordersRepository.existsByOrderTableInAndOrderStatusIn(any(), any())).willReturn(false);

        //when
        tableGroupService.ungroup(0L);

        //then
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("단체 지정 내 주문 상태가 조리 혹은 식사 인 주문 테이블이 포함되어 있을 경우 해제할 수 없다.")
    void ungroup_failed_1() {
        //given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable(1L, tableGroup, 5, false);
        OrderTable orderTable2 = new OrderTable(2L, tableGroup, 1, false);
        tableGroup.add(orderTable1);
        tableGroup.add(orderTable2);
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        given(ordersRepository.existsByOrderTableInAndOrderStatusIn(any(), any())).willReturn(true);

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(0L)).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
