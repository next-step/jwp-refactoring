package kitchenpos.table.application;

import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TableGroupServiceTest {

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private List<OrderTable> orderTables;
    private Map<Long, OrderTable> orderTableMap;
    private TableGroup 테이블그룹;

    @MockBean
    private OrderTableRepository orderTableRepository;
    @MockBean
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private ApplicationEventPublisher publisher;
    private TableService tableService;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        테이블그룹 = new TableGroup(1L, LocalDateTime.now());
        주문테이블1 = new OrderTable(1L, 테이블그룹, new NumberOfGuests(4), false);
        주문테이블2 = new OrderTable(2L, 테이블그룹, new NumberOfGuests(4), false);
        orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        orderTableMap = orderTables.stream()
                .collect(Collectors.toMap(OrderTable::getId, orderTable -> orderTable));

        tableService = new TableService(orderTableRepository, publisher);
        tableGroupService = new TableGroupService(tableGroupRepository, tableService);
    }

    @DisplayName("테이블그룹 생성 테스트")
    @Test
    void createTableGroupTest() {
        //given
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now());
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        final List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);

        when(tableGroupRepository.save(any(TableGroup.class)))
                .thenReturn(테이블그룹);
        for (OrderTable orderTable : orderTables) {
            when(orderTableRepository.findById(orderTable.getId()))
                    .thenReturn(Optional.ofNullable(orderTable));
        }
        when(orderTableRepository.findAllByTableGroupId(테이블그룹.getId()))
                .thenReturn(orderTables);

        //when
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final TableGroupResponse result = tableGroupService.create(
                new TableGroupRequest(orderTableIds));

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(테이블그룹.getId()),
                () -> assertThat(orderTableResponsesToIds(result.getOrderTables()))
                        .containsAll(orderTableToIds(orderTables)),
                () -> assertThat(orderTableResponsesToBooleans(result.getOrderTables()))
                        .allMatch(empty -> !empty),
                () -> assertThat(orderTableResponsesToTableGroupIds(result.getOrderTables()))
                        .allMatch(테이블그룹.getId()::equals)
        );
    }

    private List<Long> orderTableResponsesToIds(List<OrderTableResponse> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());
    }

    private List<Long> orderTableToIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private List<Boolean> orderTableResponsesToBooleans(List<OrderTableResponse> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::isEmpty)
                .collect(Collectors.toList());
    }

    private List<Long> orderTableResponsesToTableGroupIds(List<OrderTableResponse> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::getTableGroupId)
                .collect(Collectors.toList());
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
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now());
        final OrderTable 주문테이블1 = new OrderTable(1L, 테이블그룹, new NumberOfGuests(4), false);
        final OrderTable 주문테이블2 = new OrderTable(2L, 테이블그룹, new NumberOfGuests(4), false);
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

    @DisplayName("그룹해제 테스트")
    @Test
    void unGroupTableTest() {
        //given
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        when(orderTableRepository.findAllByTableGroupId(테이블그룹.getId()))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        when(tableGroupRepository.findById(테이블그룹.getId()))
                .thenReturn(Optional.ofNullable(테이블그룹));


        //when
        tableGroupService.ungroup(테이블그룹.getId());

        //then
        assertThat(orderTables.stream()
                .map(OrderTable::getTableGroup)
                .collect(Collectors.toList()))
                .allMatch(Objects::isNull);
    }

}
