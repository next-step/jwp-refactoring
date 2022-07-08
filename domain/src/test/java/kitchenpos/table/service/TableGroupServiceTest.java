package kitchenpos.table.service;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
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
    OrderService orderService;

    @Mock
    TableService tableService;

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
        when(tableService.findById(new_주문테이블1.getId()))
                .thenReturn(new_주문테이블1);
        when(tableService.findById(new_주문테이블2.getId()))
                .thenReturn(new_주문테이블2);
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
        OrderTable new_주문테이블1 = 빈_주문테이블_1_생성();
        when(tableService.findById(1L))
                .thenThrow(new IllegalArgumentException());
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(1L, Arrays.asList(1L));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 리스트에 단체 지정이 되지 않은 경우 등록 불가")
    @Test
    void createTableGroupAndIsNotTableGroupId() {
        // given
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(1L, Arrays.asList(1L));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroupTest() {
        // given
        주문_테이블_생성(주문테이블1, 1L);
        주문_테이블_생성(주문테이블2, 2L);
        TableGroup 단체지정1 = TableGroup.of(Arrays.asList(주문테이블1, 주문테이블2));

        when(tableGroupRepository.findById(단체지정1.getId()))
                .thenReturn(Optional.of(단체지정1));
        when(orderService.existsNotCompletesByOrderTableIdIn(Arrays.asList(1L, 2L)))
                .thenReturn(false);

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
        주문_테이블_생성(주문테이블1, 1L);
        주문_테이블_생성(주문테이블2, 2L);
        TableGroup 단체지정1 = 단체지정_1_생성(Arrays.asList(주문테이블1, 주문테이블2));
        when(tableGroupRepository.findById(단체지정1.getId()))
                .thenReturn(Optional.of(단체지정1));
        when(orderService.existsNotCompletesByOrderTableIdIn(Arrays.asList(1L, 2L)))
                .thenReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체지정1.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
