package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderService orderService;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    private TableService tableService;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderService, orderTableRepository);
        tableGroupService = new TableGroupService(orderService, tableService, tableGroupRepository);
    }

    @DisplayName("테이블그룹 생성 테스트")
    @Test
    void createTableGroupTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        final List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(),
                new OrderTables(orderTables));

        for (OrderTable orderTable : orderTables) {
            when(orderTableRepository.findById(orderTable.getId()))
                    .thenReturn(Optional.ofNullable(orderTable));
        }
        given(tableGroupRepository.save(any(TableGroup.class)))
                .willReturn(테이블그룹);

        //when
        final TableGroupResponse result = tableGroupService.create(
                new TableGroupRequest(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList())));

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(테이블그룹.getId()),
                () -> assertThat(result.getOrderTables().stream().map(OrderTableResponse::getId).collect(Collectors.toList()))
                        .containsAll(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList())),
                () -> assertThat(result.getOrderTables().stream().map(OrderTableResponse::isEmpty).collect(Collectors.toList()))
                        .allMatch(empty -> !empty),
                () -> assertThat(result.getOrderTables().stream().map(OrderTableResponse::getTableGroupId).collect(Collectors.toList()))
                        .allMatch(테이블그룹.getId()::equals)
        );
    }

    @DisplayName("테이블 목록이 비어있을 경우 테이블그룹 생성 오류 발생")
    @Test
    void createTableGroupSizeZeroExceptionTest() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(Arrays.asList())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 목록 개수가 1인 경우 테이블그룹 생성 오류 발생")
    @Test
    void createTableGroupSizeOneExceptionTest() {
        //given
        final OrderTable orderTable = new OrderTable(1L, null, new NumberOfGuests(4), true);

        //when
        when(orderTableRepository.findById(orderTable.getId()))
                .thenReturn(Optional.ofNullable(orderTable));

        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(Arrays.asList(orderTable.getId()))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 목록에 있는 테이블이 존재하지 않는 경우 테이블그룹 생성 오류 발생")
    @Test
    void createTableGroupContainNotExistTableExceptionTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        final List<Long> ids = Arrays.asList(주문테이블1.getId(), 주문테이블2.getId());

        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(null));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(ids)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 테이블로 테이블그룹 생성할 경우 오류 발생 테스트")
    @Test
    void createTableGroupEmptyTableExceptionTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), false);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), false);
        List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        List<Long> ids = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        for (OrderTable orderTable : orderTables) {
            when(orderTableRepository.findById(orderTable.getId()))
                    .thenReturn(Optional.ofNullable(orderTable));
        }

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(ids)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹이 이미 존재하는 테이블로 테이블그룹을 생성할 경우 오류 발생 테스트")
    @Test
    void createTableGroupAlreadyExistTableGroupTableExceptionTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), new OrderTables(orderTables));
        테이블그룹.group();

        List<Long> ids = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        for (OrderTable orderTable : orderTables) {
            when(orderTableRepository.findById(orderTable.getId()))
                    .thenReturn(Optional.ofNullable(orderTable));
        }

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(ids)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void unGroupTableTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), new OrderTables(orderTables));
        테이블그룹.group();

        final Order 주문1 = new Order(1L, 주문테이블1.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList());
        final Order 주문2 = new Order(2L, 주문테이블2.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList());

        List<Long> ids = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        when(tableGroupRepository.findById(테이블그룹.getId()))
                .thenReturn(Optional.ofNullable(테이블그룹));
        when(orderService.findOrderByOrderTableIds(ids))
                .thenReturn(Arrays.asList(주문1, 주문2));

        //when
        tableGroupService.ungroup(테이블그룹.getId());

        //then
        assertThat(orderTables.stream().map(OrderTable::getTableGroup).collect(Collectors.toList()))
                .allMatch(Objects::isNull);
    }

    @DisplayName("조리중이거나 식사중인 테이블을 테이블그룹에서 해제할 경우 오류발생 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void createTableGroupContainCookingOrMealTableExceptionTest(String orderStatus) {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), new OrderTables(orderTables));
        테이블그룹.group();

        final Order 주문1 = new Order(1L, 주문테이블1.getId(), orderStatus, LocalDateTime.now(), Arrays.asList());
        final Order 주문2 = new Order(2L, 주문테이블2.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList());

        List<Long> ids = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        when(tableGroupRepository.findById(테이블그룹.getId()))
                .thenReturn(Optional.ofNullable(테이블그룹));
        when(orderService.findOrderByOrderTableIds(ids))
                .thenReturn(Arrays.asList(주문1, 주문2));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
