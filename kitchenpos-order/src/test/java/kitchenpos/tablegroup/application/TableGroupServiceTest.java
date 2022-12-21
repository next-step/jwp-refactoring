package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.message.OrderTableMessage;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableRequest;
import kitchenpos.tablegroup.message.TableGroupMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹 지정시 성공하고 그룹 정보를 반환한다")
    void createTableGroupThenReturnResponseTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(
                new TableRequest(1L),
                new TableRequest(2L),
                new TableRequest(3L)
        ));
        TableGroup tableGroup = TableGroup.empty();
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        // when
        TableGroupResponse response = tableGroupService.create(request);

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
        then(tableGroupRepository).should(times(1)).save(any());

        boolean isNotEmptyAllTables = response.getOrderTables().stream().noneMatch(OrderTableResponse::isEmpty);
        assertThat(isNotEmptyAllTables).isTrue();
    }

    @Test
    @DisplayName("테이블 그룹 지정시 주문 테이블이 2개 미만인경우 예외처리되어 지정에 실패한다")
    void createTableGroupThrownByLessThanTwoTablesTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(OrderTable.of(1, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(new TableRequest(1L)));
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(TableGroup.empty());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.GROUP_ERROR_MORE_THAN_TWO_ORDER_TABLES.message());

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
        then(tableGroupRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("테이블 그룹 지정시 등록된 주문 테이블 개수가 다른경우 예외처리되어 지정에 실패한다")
    void createTableGroupThrownByNotEqualTableSizeTest() {
        // given
        List<OrderTable> storedOrderTables = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(
                new TableRequest(1L),
                new TableRequest(2L)
        ));
        given(orderTableRepository.findAllById(any())).willReturn(storedOrderTables);

        // when
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupMessage.CREATE_ERROR_NOT_EQUAL_TABLE_SIZE.message());

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
    }

    @Test
    @DisplayName("테이블 그룹 지정시 주문 테이블이 사용중인경우 예외처리되어 지정에 실패한다")
    void createTableGroupThrownByTableIsNotEmptyTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(1, false), // 사용중인 주문 테이블
                OrderTable.of(2, true)
        );
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(
                new TableRequest(1L),
                new TableRequest(2L)
        ));
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(TableGroup.empty());

        // when
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.GROUP_ERROR_ORDER_TABLE_IS_NOT_EMPTY.message());

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
        then(tableGroupRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("테이블 그룹 지정시 주문 테이블이 다른 그룹에 속한경우 예외처리되어 지정에 실패한다")
    void createTableGroupThrownByEnrolledOtherGroupTest() {
        // given
        OrderTable orderTable = OrderTable.of(1, true);
        OrderTables otherOrderTables = new OrderTables(Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        ));
        otherOrderTables.groupBy(1L);

        List<OrderTable> orderTables = Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        );
        TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(
                new TableRequest(1L),
                new TableRequest(2L)
        ));
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(TableGroup.empty());

        // when
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.GROUP_ERROR_OTHER_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());

        // then
        then(orderTableRepository).should(times(1)).findAllById(any());
        then(tableGroupRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("테이블 그룹 해지에 성공한다")
    void unGroupTablesTest() {
        // given
        List<OrderTable> orderTableItems = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true)
        );
        OrderTables orderTables = new OrderTables(orderTableItems);
        orderTables.groupBy(1L);
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(TableGroup.empty()));
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(orderTableItems);
        willDoNothing().given(orderValidator).validateUnGroup(orderTables);

        // when
        tableGroupService.ungroup(1L);

        // then
        then(tableGroupRepository).should(times(1)).findById(any());
        then(orderTableRepository).should(times(1)).findAllByTableGroupId(any());
        then(orderValidator).should(times(1)).validateUnGroup(any());
    }

    @Test
    @DisplayName("테이블 그룹 해지시 속해져있는 테이블중 조리 또는 식사 상태인경우 예외처리되어 해지에 실패한다")
    void unGroupTablesThrownByCookingOrMealOrderStatesTest() {
        // given
        List<OrderTable> orderTableItems = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true)
        );
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(TableGroup.empty()));
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(orderTableItems);
        willThrow(new IllegalArgumentException(OrderTableMessage.UN_GROUP_ERROR_INVALID_ORDER_STATE.message()))
                .given(orderValidator).validateUnGroup(any());

        // when
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.UN_GROUP_ERROR_INVALID_ORDER_STATE.message());

        // then
        then(tableGroupRepository).should(times(1)).findById(any());
        then(orderTableRepository).should(times(1)).findAllByTableGroupId(any());
        then(orderValidator).should(times(1)).validateUnGroup(any());
    }
}
