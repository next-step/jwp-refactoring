package kitchenpos.table.service;

import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("테이블 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void 테이블_그룹_생성() {
        OrderTable firstOrderTable = OrderTable.of(1, true);
        OrderTable secondOrderTable = OrderTable.of(2, true);
        TableGroup tableGroup = TableGroup.create();

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()));
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        given(orderTableRepository.findAllById(orderTableIds)).willReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);
        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);

        TableGroupResponse response = tableGroupService.create(tableGroupRequest);

        assertThat(response).isNotNull();
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        TableGroup tableGroup = TableGroup.create();

        given(tableGroupRepository.findById(tableGroup.getId())).willReturn(Optional.of(tableGroup));

        tableGroupService.ungroup(tableGroup.getId());

        verify(tableGroupRepository).delete(tableGroup);
    }

    @DisplayName("그룹핑 목표 주문 테이블 최소 개수 체크")
    @Test
    void 주문_테이블_갯수_예외() {
        OrderTable firstOrderTable = OrderTable.of(1, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.singletonList(firstOrderTable.getId()));
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        given(orderTableRepository.findAllById(orderTableIds)).willReturn(Collections.singletonList(firstOrderTable));

        Throwable thrown = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("주문 테이블 2개 이상 그룹화할 수 있습니다.");
    }

    @DisplayName("그룹핑 목표 주문 테이블이 이미 그룹핑되어 경우 예외")
    @Test
    void 주문_테이블_이미_그룹핑_됨_예외() {
        OrderTable firstOrderTable = OrderTable.of(1, true);
        OrderTable secondOrderTable = OrderTable.of(2, true);
        secondOrderTable.addGroup(1L);
        TableGroup tableGroup = TableGroup.create();

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()));
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        given(orderTableRepository.findAllById(orderTableIds)).willReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);
        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);

        Throwable thrown = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("이미 그룹이 존재하는 주문 테이블입니다.");
    }

    @DisplayName("그룹핑 목표 주문 테이블이 이미 그룹핑되어 경우 예외")
    @Test
    void 완료_안된_주문_테이블_존재_예외() {
        OrderTable firstOrderTable = OrderTable.of(1, true);
        OrderTable secondOrderTable = OrderTable.of(2, true);
        TableGroup tableGroup = TableGroup.create();

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()));
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        given(orderTableRepository.findAllById(orderTableIds)).willReturn(Arrays.asList(firstOrderTable, secondOrderTable));

        Order order = Order.of(firstOrderTable.getId(), Collections.singletonList(OrderLineItem.of(1L, Quantity.of(1L))));
        given(orderRepository.findOrderByOrderTableId(firstOrderTable.getId())).willReturn(Collections.singletonList(order));
        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);
        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);

        Throwable thrown = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("완료되지 않은 주문이 존재합니다.");
    }

    @DisplayName("테이블 그룹이 미존재하여 해제 예외")
    @Test
    void 테이블_그룹_미존재_시_해제_예외() {
        TableGroup tableGroup = TableGroup.create();

        given(tableGroupRepository.findById(tableGroup.getId())).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> tableGroupService.ungroup(tableGroup.getId()));

        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage("해당 테이블 그룹을 찾을 수 없습니다.");
    }

    @DisplayName("완료되지 않은 주문이 존재하여 그룹 해제 예외")
    @Test
    void 완료되지_않은_주문_존재_시_해제_예외() {
        TableGroup tableGroup = TableGroup.create();
        Order order = Order.of(1L, Collections.singletonList(OrderLineItem.of(1L, Quantity.of(1L))));

        given(tableGroupRepository.findById(tableGroup.getId())).willReturn(Optional.of(tableGroup));
        given(orderTableRepository.findOrderTableByTableGroupId(null)).willReturn(Collections.singletonList(OrderTable.of(10, false)));
        given(orderRepository.findOrderByOrderTableId(null)).willReturn(Collections.singletonList(order));

        Throwable thrown = catchThrowable(() -> tableGroupService.ungroup(tableGroup.getId()));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("완료되지 않은 주문이 존재하여 테이블 그룹을 해제할 수 없습니다.");
    }
}
