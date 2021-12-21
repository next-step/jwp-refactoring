package kitchenpos.order.application;

import static kitchenpos.order.application.TableServiceTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
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
        assertThrows(BadRequestException.class, () ->
            tableGroupService.create(tableGroupRequest));
        verify(orderTableRepository).findAllByIdIn(orderTableIds);
        verify(tableGroupRepository, times(0)).save(tableGroup);
    }

    @Disabled
    @DisplayName("주문 테이블 그룹에 속해있지 않은 주문 테이블만 등록 가능하다.")
    @Test
    void createTableGroupExistTableGroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, 0, true),
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

    private TableGroup 주문_테이블_그룹_생성(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }
}
