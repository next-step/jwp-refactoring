package kitchenpos.table.service;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static kitchenpos.util.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderTable 주문테이블3;
    private OrderTable 주문테이블4;

    @BeforeEach
    void setUp() {
        주문테이블1 = 빈_주문테이블_1_생성();
        주문테이블2 = 빈_주문테이블_2_생성();
        주문테이블3 = 빈_주문테이블_3_생성();
    }

    @DisplayName("단체 지정 등록")
    @Test
    void createTableGroup() {
        // given
        OrderTable new_주문테이블1 = 빈_주문테이블_1_생성();
        OrderTable new_주문테이블2 = 빈_주문테이블_2_생성();
        when(orderTableRepository.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(new_주문테이블1, new_주문테이블2));
        when(tableGroupRepository.save(any()))
                .thenReturn(TableGroup.of(1L, Arrays.asList(주문테이블1, 주문테이블2)));

        // when
        TableGroupResponse result = tableGroupService.create(new TableGroupRequest(1L, Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())));

        // then
        assertThat(result.getOrderTables()).hasSize(2);

        List<OrderTableResponse> list = result.getOrderTables();
        assertThat(list.stream().map(OrderTableResponse::isEmpty).collect(Collectors.toList())).containsExactly(false, false);
    }

    @DisplayName("주문 테이블 리스트가 `2` 보다 작은 경우 등록 불가")
    @Test
    void createTableGroupAndOrderTableListSizeOne() {
        // given
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(null, Arrays.asList(주문테이블1.getId()));
        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 리스트가 없는 경우 등록 불가")
    @Test
    void createTableGroupAndOrderTableNull() {
        // given
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(null, Collections.EMPTY_LIST);

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블이 있는 경우 등록 불가")
    @Test
    void createTableGroupAndOrderTableNotSave() {
        // given
        주문테이블1 = 주문테이블_1_생성();
        when(orderTableRepository.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(주문테이블1));
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(null, Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));
        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 리스트에 단체 지정이 되지 않은 경우 등록 불가")
    @Test
    void createTableGroupAndIsNotTableGroupId() {
        // given
        주문테이블1 = 주문테이블_1_생성();
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(1L, Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroupTest() {
        // given
        TableGroup 단체지정1 = TableGroup.of(Arrays.asList(주문테이블1, 주문테이블2));

        when(tableGroupRepository.findById(단체지정1.getId()))
                .thenReturn(Optional.of(단체지정1));
        when(orderTableRepository.findAllByTableGroup(단체지정1))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        // when
        tableGroupService.ungroup(단체지정1.getId());

        // then
        List<OrderTable> list = 단체지정1.getOrderTables().stream()
                .filter(it -> Objects.nonNull(it.getTableGroupId()))
                .collect(Collectors.toList());

        assertThat(list).isEmpty();
    }

    @DisplayName("주문 테이블 중 `조리`, `식사` 상태인 경우 해제 불가")
    @Test
    void ungroupAndCoolingOrMealStatus() {
        TableGroup 단체지정1 = 단체지정_1_생성(Arrays.asList(주문테이블1, 주문테이블2));
        when(tableGroupRepository.findById(단체지정1.getId()))
                .thenReturn(Optional.of(단체지정1));
        when(orderTableRepository.findAllByTableGroup(단체지정1))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderRepository.existsByOrderTableInAndOrderStatusIn(
                Arrays.asList(주문테이블1, 주문테이블2),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        )).thenReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체지정1.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
