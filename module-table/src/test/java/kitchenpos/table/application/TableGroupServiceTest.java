package kitchenpos.table.application;

import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableService orderTableService;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;
    private TableGroupService validateTableGroupService;

    private OrderTableRequest 주문_테이블_request;
    private OrderTableRequest 주문_테이블_2_request;
    private TableGroupRequest 테이블_그룹_request;

    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블2;

    @BeforeEach
    void setUp() {
        주문_테이블_request = new OrderTableRequest(1L, null, 3, true);
        주문_테이블_2_request = new OrderTableRequest(2L, null, 5, true);
        테이블_그룹_request = new TableGroupRequest(1L, Arrays.asList(주문_테이블_request.getId(), 주문_테이블_2_request.getId()));

        주문_테이블 = OrderTable.of(1L, null, 3, true);
        주문_테이블2 = OrderTable.of(2L, null, 5, true);

        validateTableGroupService = new TableGroupService(orderTableService, tableGroupRepository);
    }

    @DisplayName("주문 테이블을 단체지정하면 정상적으로 단체지정 되어야한다")
    @Test
    void create_test() {
        // given
        OrderTable new_주문_테이블 = OrderTable.of(1L, null, 3, true);
        OrderTable new_주문_테이블2 = OrderTable.of(2L, null, 3, true);
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(new_주문_테이블, new_주문_테이블2);

        // given
        when(orderTableService.findOrderTablesByIdIn(Arrays.asList(주문_테이블_request.getId(), 주문_테이블_2_request.getId())))
            .thenReturn(주문_테이블_목록);

        when(tableGroupRepository.save(any()))
            .thenReturn(TableGroup.of(1L));

        // when
        TableGroupResponse result = tableGroupService.create(테이블_그룹_request);

        // then
        List<OrderTableResponse> 그룹_ID_있는_테이블 = result.getOrderTables().stream()
                .filter(it -> Objects.nonNull(it.getTableGroupId()))
                .collect(Collectors.toList());

        assertAll(
            () -> assertThat(result.getCreatedDate()).isNotNull(),
            () -> assertThat(그룹_ID_있는_테이블).hasSize(2),
            () -> assertThat(result.getOrderTables().size()).isEqualTo(
                테이블_그룹_request.getOrderTableIds().size())
        );
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 없거나 2개 미만이면 예외가 발생한다")
    @Test
    void create_exception_test() {
        // given
        테이블_그룹_request = new TableGroupRequest(null, Collections.emptyList());

        // then
        assertThatThrownBy(() -> {
            validateTableGroupService.create(테이블_그룹_request);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.ORDER_TABLE_AT_LEAST_TWO.getMessage());
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 없거나 2개 미만이면 예외가 발생한다")
    @Test
    void create_exception_test2() {
        // given
        테이블_그룹_request = new TableGroupRequest(null, Collections.singletonList(주문_테이블_request.getId()));

        // then
        assertThatThrownBy(() -> {
            validateTableGroupService.create(테이블_그룹_request);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.ORDER_TABLE_AT_LEAST_TWO.getMessage());
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 모두 존재하지 않으면 예외가 발생한다")
    @Test
    void create_exception_test3() {
        // given
        when(orderTableService.findOrderTablesByIdIn(Arrays.asList(주문_테이블_request.getId(), 주문_테이블_2_request.getId())))
            .thenReturn(Collections.singletonList(주문_테이블));

        // then
        assertThatThrownBy(() -> {
            validateTableGroupService.create(테이블_그룹_request);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.CONTAINS_NOT_EXIST_ORDER_TABLE.getMessage());
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 비어있지 않거나 "
        + "이미 테이블 그룹이 존재하면 예외가 발생한다")
    @Test
    void create_exception_test4() {
        // given
        주문_테이블 = OrderTable.of(1L, null, 3, false);
        주문_테이블2 = OrderTable.of(2L, 1L, 3, true);
        when(orderTableService.findOrderTablesByIdIn(Arrays.asList(주문_테이블.getId(), 주문_테이블2.getId())))
            .thenReturn(Arrays.asList(주문_테이블, 주문_테이블2));

        // then
        assertThatThrownBy(() -> {
            validateTableGroupService.create(테이블_그룹_request);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.MUST_NOT_BE_EMPTY_OR_GROUPED_TABLE.getMessage());
    }

    @DisplayName("테이블 단체지정을 해제하면 정상적으로 해제되어야 한다")
    @Test
    void ungroup_test() {
        // given
        TableGroup 테이블_그룹 = TableGroup.of(1L);
        테이블_그룹.mapIntoTable(Arrays.asList(주문_테이블, 주문_테이블2));

        when(tableGroupRepository.findById(테이블_그룹.getId()))
            .thenReturn(Optional.of(테이블_그룹));

        주문_테이블.unGroup();
        주문_테이블2.unGroup();
        when(tableGroupRepository.save(테이블_그룹))
            .thenReturn(테이블_그룹);

        // when
        tableGroupService.ungroup(테이블_그룹.getId());

        // then
        assertNull(주문_테이블.getTableGroupId());
        assertNull(주문_테이블2.getTableGroupId());
    }
}
