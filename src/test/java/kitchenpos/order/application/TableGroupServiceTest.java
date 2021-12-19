package kitchenpos.order.application;

import static kitchenpos.order.application.TableServiceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;

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
        tableGroupService = new TableGroupService(
            orderRepository, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("주문 테이블 그룹을 등록할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        OrderTable orderTable1 = 주문_테이블_생성(1L, 0, true);
        List<OrderTable> orderTables = Arrays.asList(
            orderTable1,
            주문_테이블_생성(2L, 0, true));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
            orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

        given(orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTables()))
            .willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        given(orderTableRepository.save(orderTable1)).willReturn(null);

        // when
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(savedTableGroup.getOrderTables())
            .extracting("empty")
            .containsExactly(false, false);
    }

    @DisplayName("주문 테이블 그룹은 2 테이블 이상이어야 등록할 수 있다.")
    @Test
    void createTableGroupMinSize() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(
            주문_테이블_생성(2L, 0, true));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.singletonList(2L));

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableGroupService.create(tableGroupRequest));
        verify(orderTableRepository, times(0)).findAllByIdIn(tableGroupRequest.getOrderTables());
    }

    @DisplayName("조회 가능한 주문 테이블만 주문 테이블 그룹에 등록할 수 있다.")
    @Test
    void createTableGroupNotFoundTable() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, 0, true),
            주문_테이블_생성(2L, 0, true));
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        TableGroup tableGroup = 주문_테이블_그룹_생성(orderTables);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
            orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        given(orderTableRepository.findAllByIdIn(orderTableIds))
            .willReturn(Collections.emptyList());

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableGroupService.create(tableGroupRequest));
        verify(orderTableRepository).findAllByIdIn(orderTableIds);
        verify(tableGroupRepository, times(0)).save(tableGroup);
    }

    @DisplayName("빈 주문 테이블만 주문 테이블 그룹에 등록할 수 있다.")
    @Test
    void createTableGroupNotEmptyTable() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, 0, true),
            주문_테이블_생성(2L, 0, false));
        List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId)
            .collect(Collectors.toList());
        TableGroup tableGroup = 주문_테이블_그룹_생성(orderTables);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
            orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(orderTables);

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableGroupService.create(tableGroupRequest));
        verify(orderTableRepository).findAllByIdIn(orderTableIds);
        verify(tableGroupRepository, times(0)).save(tableGroup);
    }

    @DisplayName("주문 테이블 그룹에 속해있지 않은 주문 테이블만 등록 가능하다.")
    @Test
    void createTableGroupExistTableGroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, 1L, 0, true),
            주문_테이블_생성(2L, 0, true));
        List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId)
            .collect(Collectors.toList());
        TableGroup tableGroup = 주문_테이블_그룹_생성(orderTables);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
            orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(orderTables);

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableGroupService.create(tableGroupRequest));
        verify(orderTableRepository).findAllByIdIn(orderTableIds);
        verify(tableGroupRepository, times(0)).save(tableGroup);
    }

    @DisplayName("주문 테이블 그룹에서 주문 테이블을 삭제할 수 있다.")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, tableGroupId, 0, true),
            주문_테이블_생성(2L, tableGroupId, 0, true));
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        given(orderTableRepository.findAllByTableGroupId(tableGroupId))
            .willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(false);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        assertThat(orderTables).extracting("tableGroupId")
            .containsExactly(null, null);
    }

    private TableGroup 주문_테이블_그룹_생성(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }
}
